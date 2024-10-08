package DAOPsql;

import DAO.OVChipkaartDAO;
import DAO.ProductDAO;
import DAO.ReizigerDAO;
import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    private Connection conn;
    private ReizigerDAOPsql rDao;
    private ProductDAO productDAO;  // Add ProductDAO to manage relationships

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        OVChipkaart existingOVChipkaart = findById(ovChipkaart.getKaartNummer());
        if (existingOVChipkaart != null) {
            System.out.println("OVChipkaart with kaart_nummer " + ovChipkaart.getKaartNummer() + " already exists. Updating instead.");
            return update(ovChipkaart);  // If it exists, update the existing record
        }

        // If it does not exist, insert a new OVChipkaart
        String query = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, ovChipkaart.getKaartNummer());
        statement.setDate(2, ovChipkaart.getGeldigTot());
        statement.setInt(3, ovChipkaart.getKlasse());
        statement.setDouble(4, ovChipkaart.getSaldo());
        statement.setInt(5, ovChipkaart.getReiziger().getId());
        statement.executeUpdate();
        statement.close();

        // Save associated products in the ov_chipkaart_product table
        for (Product product : ovChipkaart.getProducts()) {
            linkOVChipkaartProduct(ovChipkaart, product);
        }

        return true;
    }

    private void linkOVChipkaartProduct(OVChipkaart ovChipkaart, Product product) throws SQLException {
        String query = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, ovChipkaart.getKaartNummer());
        pst.setInt(2, product.getProductNummer());
        pst.executeUpdate();
        pst.close();
    }

    private void unlinkAllProducts(OVChipkaart ovChipkaart) throws SQLException {
        String query = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, ovChipkaart.getKaartNummer());
        pst.executeUpdate();
        pst.close();
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        String query = "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setDate(1, ovChipkaart.getGeldigTot());
        statement.setInt(2, ovChipkaart.getKlasse());
        statement.setDouble(3, ovChipkaart.getSaldo());
        statement.setInt(4, ovChipkaart.getReiziger().getId());
        statement.setInt(5, ovChipkaart.getKaartNummer());
        statement.executeUpdate();
        statement.close();

        // Update the relationship with products
        unlinkAllProducts(ovChipkaart);
        for (Product product : ovChipkaart.getProducts()) {
            linkOVChipkaartProduct(ovChipkaart, product);
        }

        return true;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        // First, delete the relationship with products
        unlinkAllProducts(ovChipkaart);

        // Then delete the OVChipkaart itself
        String query = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, ovChipkaart.getKaartNummer());
        statement.executeUpdate();
        statement.close();

        return true;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        String query = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, reiziger.getId());
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            OVChipkaart ovChipkaart = new OVChipkaart();
            ovChipkaart.setKaartNummer(rs.getInt("kaart_nummer"));
            ovChipkaart.setGeldigTot(rs.getDate("geldig_tot"));
            ovChipkaart.setKlasse(rs.getInt("klasse"));
            ovChipkaart.setSaldo(rs.getDouble("saldo"));
            ovChipkaart.setReiziger(reiziger);

            // Fetch associated products
            List<Product> producten = productDAO.findByOVChipkaart(ovChipkaart);
            ovChipkaart.setProducts(producten);

            ovChipkaarten.add(ovChipkaart);
        }
        rs.close();
        statement.close();
        return ovChipkaarten;
    }

    @Override
    public OVChipkaart findById(int id) throws SQLException {
        String query = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        OVChipkaart ovChipkaart = null;

        if (rs.next()) {
            int reizigerId = rs.getInt("reiziger_id");

            ovChipkaart = new OVChipkaart();
            ovChipkaart.setKaartNummer(rs.getInt("kaart_nummer"));
            ovChipkaart.setGeldigTot(rs.getDate("geldig_tot"));
            ovChipkaart.setKlasse(rs.getInt("klasse"));
            ovChipkaart.setSaldo(rs.getDouble("saldo"));
            ovChipkaart.setReiziger(rDao.findById(reizigerId));

            // Fetch associated products
            List<Product> producten = productDAO.findByOVChipkaart(ovChipkaart);
            ovChipkaart.setProducts(producten);
        }
        rs.close();
        statement.close();
        return ovChipkaart;
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        String query = "SELECT * FROM ov_chipkaart";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int reizigerId = rs.getInt("reiziger_id");

            OVChipkaart ovChipkaart = new OVChipkaart();
            ovChipkaart.setKaartNummer(rs.getInt("kaart_nummer"));
            ovChipkaart.setGeldigTot(rs.getDate("geldig_tot"));
            ovChipkaart.setKlasse(rs.getInt("klasse"));
            ovChipkaart.setSaldo(rs.getDouble("saldo"));

            // Fetch associated Reiziger and Products
            ovChipkaart.setReiziger(rDao.findById(reizigerId));
            List<Product> producten = productDAO.findByOVChipkaart(ovChipkaart);
            ovChipkaart.setProducts(producten);

            ovChipkaarten.add(ovChipkaart);
        }
        rs.close();
        statement.close();
        return ovChipkaarten;
    }


    public void setReizigerDAO(ReizigerDAO rDao) {
        this.rDao = (ReizigerDAOPsql) rDao;
    }

    public void setProductDAO(ProductDAO pDao) {
        this.productDAO = pDao;  // Add ProductDAO to manage product relations
    }
}
