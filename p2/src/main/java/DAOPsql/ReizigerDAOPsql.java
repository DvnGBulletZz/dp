package DAOPsql;

import DAO.ReizigerDAO;
import domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }
    // using try with resources to close the connection
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
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        String query = "DELETE FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, reiziger.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        Reiziger reiziger = null;
        String query = "SELECT * FROM reiziger WHERE reiziger_id = ?";
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
        String query = "SELECT * FROM reiziger WHERE geboortedatum = ?";
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
        String query = "SELECT * FROM reiziger";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Reiziger reiziger = new Reiziger();
                reiziger.setId(rs.getInt("reiziger_id"));
                reiziger.setVoorletters(rs.getString("voorletters"));
                reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
                reiziger.setAchternaam(rs.getString("achternaam"));
                reiziger.setGeboortedatum(rs.getDate("geboortedatum"));
                reizigerList.add(reiziger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigerList;
    }
}