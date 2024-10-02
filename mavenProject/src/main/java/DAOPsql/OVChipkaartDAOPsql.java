package DAOPsql;

import DAO.OVChipkaartDAO;
import DAO.ReizigerDAO;
import domain.OVChipkaart;
import domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    private Connection conn;
    private ReizigerDAOPsql rDao;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;

    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        String query = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, ovChipkaart.getKaartNummer());
        statement.setDate(2, ovChipkaart.getGeldigTot());
        statement.setInt(3, ovChipkaart.getKlasse());
        statement.setDouble(4, ovChipkaart.getSaldo());
        statement.setInt(5, ovChipkaart.getReiziger().getId());
        statement.executeUpdate();
        statement.close();
        return true;
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
        return true;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
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
            ovChipkaart.setGeldigTot(Date.valueOf(rs.getString("geldig_tot")));
            ovChipkaart.setKlasse(rs.getInt("klasse"));
            ovChipkaart.setSaldo(rs.getDouble("saldo"));
            ovChipkaart.setReiziger(reiziger);
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
            ovChipkaart.setGeldigTot(Date.valueOf(rs.getString("geldig_tot")));
            ovChipkaart.setKlasse(rs.getInt("klasse"));
            ovChipkaart.setSaldo(rs.getDouble("saldo"));
            ovChipkaart.setReiziger(rDao.findById(reizigerId));
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
            ovChipkaart.setGeldigTot(Date.valueOf(rs.getString("geldig_tot")));
            ovChipkaart.setKlasse(rs.getInt("klasse"));
            ovChipkaart.setSaldo(rs.getDouble("saldo"));
            ovChipkaart.setReiziger(rDao.findById(reizigerId)); // Fetch the associated Reiziger if needed
            ovChipkaarten.add(ovChipkaart);
        }
        rs.close();
        statement.close();
        return ovChipkaarten;
    }

    @Override
    public void setReizigerDAO(ReizigerDAO rDao) {
        this.rDao = (ReizigerDAOPsql) rDao;
    }


}