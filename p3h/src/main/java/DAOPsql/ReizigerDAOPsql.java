package DAOPsql;

import DAO.AdresDAO;
import DAO.ReizigerDAO;
import domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adresDAO;

    public ReizigerDAOPsql(Connection conn, AdresDAO adresDAO) {
        this.conn = conn;
        this.adresDAO = adresDAO;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        String query = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, reiziger.getId());
            statement.setString(2, reiziger.getVoorletters());
            statement.setString(3, reiziger.getTussenvoegsel());
            statement.setString(4, reiziger.getAchternaam());
            statement.setDate(5, reiziger.getGeboortedatum());
            statement.executeUpdate();

            if (this.adresDAO != null) {
                this.adresDAO.save(reiziger.getAdres());
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        String query = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, reiziger.getVoorletters());
            statement.setString(2, reiziger.getTussenvoegsel());
            statement.setString(3, reiziger.getAchternaam());
            statement.setDate(4, reiziger.getGeboortedatum());
            statement.setInt(5, reiziger.getId());
            statement.executeUpdate();

            if (this.adresDAO != null) {
                this.adresDAO.update(reiziger.getAdres());
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try {
            if (this.adresDAO != null) {
                this.adresDAO.delete(reiziger.getAdres());
            }

            String query = "DELETE FROM reiziger WHERE reiziger_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, reiziger.getId());
                statement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        Reiziger reiziger = null;
        String query = "SELECT reiziger.*, adres.adres_id, adres.postcode, adres.huisnummer, adres.straat, adres.woonplaats " +
                "FROM reiziger " +
                "LEFT JOIN adres ON adres.reiziger_id = reiziger.reiziger_id " +
                "WHERE reiziger.reiziger_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) {
        List<Reiziger> reizigerList = new ArrayList<>();
        String query = """
            SELECT reiziger.*, adres.adres_id, adres.postcode, adres.huisnummer, adres.straat, adres.woonplaats
            FROM reiziger
            LEFT JOIN adres ON adres.reiziger_id = reiziger.reiziger_id
            WHERE geboortedatum = ?
            """;
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setDate(1, date);
            try (ResultSet rs = statement.executeQuery()) {
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
                    reizigerList.add(reiziger);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigerList;
    }

    @Override
    public List<Reiziger> findAll() {
        List<Reiziger> reizigerList = new ArrayList<>();
        String query = """
            SELECT reiziger.*, adres.adres_id, adres.postcode, adres.huisnummer, adres.straat, adres.woonplaats
            FROM reiziger
            LEFT JOIN adres ON adres.reiziger_id = reiziger.reiziger_id
            """;
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
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
                reizigerList.add(reiziger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigerList;
    }
}