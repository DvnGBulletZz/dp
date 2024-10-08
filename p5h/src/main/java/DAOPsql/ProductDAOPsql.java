package DAOPsql;

import domain.Product;
import domain.OVChipkaart;
import DAO.ProductDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        String query = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setLong(1, product.getProductNummer());
        pst.setString(2, product.getProductNaam());
        pst.setString(3, product.getBeschrijving());
        pst.setDouble(4, product.getPrijs());
        boolean success = pst.executeUpdate() > 0;

        // Save the relationship in the junction table
        for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
            linkOVChipkaartProduct(ovChipkaart, product);
        }

        return success;
    }

    private void linkOVChipkaartProduct(OVChipkaart ovChipkaart, Product product) throws SQLException {
        String query = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setLong(1, ovChipkaart.getKaartNummer());
        pst.setLong(2, product.getProductNummer());
        pst.executeUpdate();
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        unlinkAllOVChipkaarten(product);

        String query = "DELETE FROM product WHERE product_nummer = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setLong(1, product.getProductNummer());
        return pst.executeUpdate() > 0;
    }

    private void unlinkAllOVChipkaarten(Product product) throws SQLException {
        String query = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setLong(1, product.getProductNummer());
        pst.executeUpdate();
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String query = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, product.getProductNaam());
        pst.setString(2, product.getBeschrijving());
        pst.setDouble(3, product.getPrijs());
        pst.setLong(4, product.getProductNummer());
        boolean success = pst.executeUpdate() > 0;

        unlinkAllOVChipkaarten(product);
        for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
            linkOVChipkaartProduct(ovChipkaart, product);
        }

        return success;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        List<Product> producten = new ArrayList<>();
        String query = "SELECT p.* FROM product p JOIN ov_chipkaart_product ocp ON p.product_nummer = ocp.product_nummer WHERE ocp.kaart_nummer = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setLong(1, ovChipkaart.getKaartNummer());
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            Product product = new Product(rs.getInt("product_nummer"), rs.getString("naam"), rs.getString("beschrijving"), rs.getLong("prijs"));
            producten.add(product);
        }

        return producten;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> producten = new ArrayList<>();
        String query = "SELECT * FROM product";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            Product product = new Product(rs.getInt("product_nummer"), rs.getString("naam"), rs.getString("beschrijving"), rs.getLong("prijs"));
            producten.add(product);
        }

        return producten;
    }
}
