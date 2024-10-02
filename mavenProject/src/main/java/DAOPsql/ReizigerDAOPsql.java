package DAOPsql;

import DAO.AdresDAO;
import DAO.OVChipkaartDAO;
import DAO.ReizigerDAO;
import domain.Reiziger;
import domain.OVChipkaart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adresDAO;
    private OVChipkaartDAO ovChipkaartDAO;

    public ReizigerDAOPsql(Connection conn, AdresDAO adresDAO, OVChipkaartDAO ovChipkaartDAO) {
        this.conn = conn;
        this.adresDAO = adresDAO;
        this.ovChipkaartDAO = ovChipkaartDAO;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        String query = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, reiziger.getId());
        statement.setString(2, reiziger.getVoorletters());
        statement.setString(3, reiziger.getTussenvoegsel());
        statement.setString(4, reiziger.getAchternaam());
        statement.setDate(5, reiziger.getGeboortedatum());
        statement.executeUpdate();
        statement.close();

        if (this.adresDAO != null) {
            this.adresDAO.save(reiziger.getAdres());
        }

        if (this.ovChipkaartDAO != null) {
            for (OVChipkaart ovChipkaart : reiziger.getOvChipkaarten()) {
                this.ovChipkaartDAO.save(ovChipkaart);
            }
        }

        return true;
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        String query = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, reiziger.getVoorletters());
        statement.setString(2, reiziger.getTussenvoegsel());
        statement.setString(3, reiziger.getAchternaam());
        statement.setDate(4, reiziger.getGeboortedatum());
        statement.setInt(5, reiziger.getId());
        statement.executeUpdate();
        statement.close();

        if (this.adresDAO != null) {
            this.adresDAO.update(reiziger.getAdres());
        }

        if (this.ovChipkaartDAO != null) {
            for (OVChipkaart ovChipkaart : reiziger.getOvChipkaarten()) {
                this.ovChipkaartDAO.update(ovChipkaart);
            }
        }

        return true;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        if (this.adresDAO != null) {
            this.adresDAO.delete(reiziger.getAdres());
        }

        if (this.ovChipkaartDAO != null) {
            for (OVChipkaart ovChipkaart : reiziger.getOvChipkaarten()) {
                this.ovChipkaartDAO.delete(ovChipkaart);
            }
        }

        String query = "DELETE FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, reiziger.getId());
        statement.executeUpdate();
        statement.close();

        return true;
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        Reiziger reiziger = null;
        String query = "SELECT reiziger.*, adres.adres_id, adres.postcode, adres.huisnummer, adres.straat, adres.woonplaats " +
                "FROM reiziger " +
                "LEFT JOIN adres ON adres.reiziger_id = reiziger.reiziger_id " +
                "WHERE reiziger.reiziger_id = ?";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            reiziger = new Reiziger();
            reiziger.setId(rs.getInt("reiziger_id"));
            reiziger.setVoorletters(rs.getString("voorletters"));
            reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
            reiziger.setAchternaam(rs.getString("achternaam"));
            reiziger.setGeboortedatum(rs.getDate("geboortedatum"));
            if (this.adresDAO != null) {
                reiziger.setAdres(this.adresDAO.findByReiziger(reiziger));
            }
            if (this.ovChipkaartDAO != null) {
                reiziger.setOvChipkaarten(this.ovChipkaartDAO.findByReiziger(reiziger));
            }
        }
        rs.close();
        statement.close();
        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) throws SQLException {
        List<Reiziger> reizigerList = new ArrayList<>();
        String query = """
            SELECT reiziger.*, adres.adres_id, adres.postcode, adres.huisnummer, adres.straat, adres.woonplaats 
            FROM reiziger 
            LEFT JOIN adres ON adres.reiziger_id = reiziger.reiziger_id 
            WHERE geboortedatum = ?
            """;        PreparedStatement statement = conn.prepareStatement(query);
        statement.setDate(1, date);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Reiziger reiziger = new Reiziger();
            reiziger.setId(rs.getInt("reiziger_id"));
            reiziger.setVoorletters(rs.getString("voorletters"));
            reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
            reiziger.setAchternaam(rs.getString("achternaam"));
            reiziger.setGeboortedatum(rs.getDate("geboortedatum"));
            if (this.adresDAO != null) {
                reiziger.setAdres(this.adresDAO.findByReiziger(reiziger));
            }
            if (this.ovChipkaartDAO != null) {
                reiziger.setOvChipkaarten(this.ovChipkaartDAO.findByReiziger(reiziger));
            }
            reizigerList.add(reiziger);
        }
        rs.close();
        statement.close();
        return reizigerList;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        List<Reiziger> reizigerList = new ArrayList<>();
        String query = """
            SELECT reiziger.*, adres.adres_id, adres.postcode, adres.huisnummer, adres.straat, adres.woonplaats 
            FROM reiziger 
            LEFT JOIN adres ON adres.reiziger_id = reiziger.reiziger_id
            """;
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Reiziger reiziger = new Reiziger();
            reiziger.setId(rs.getInt("reiziger_id"));
            reiziger.setVoorletters(rs.getString("voorletters"));
            reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
            reiziger.setAchternaam(rs.getString("achternaam"));
            reiziger.setGeboortedatum(rs.getDate("geboortedatum"));
            if (this.adresDAO != null) {
                reiziger.setAdres(this.adresDAO.findByReiziger(reiziger));
            }
            if (this.ovChipkaartDAO != null) {
                reiziger.setOvChipkaarten(this.ovChipkaartDAO.findByReiziger(reiziger));
            }
            reizigerList.add(reiziger);
        }
        rs.close();
        statement.close();
        return reizigerList;
    }


}