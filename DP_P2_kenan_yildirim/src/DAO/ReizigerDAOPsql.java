package DAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hu.nl.ovchip.domein.Reiziger;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection connection;

    public ReizigerDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Reiziger reiziger) throws SQLException {
        String sql = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reiziger.getId());
            stmt.setString(2, reiziger.getVoorletters());
            stmt.setString(3, reiziger.getTussenvoegsel());
            stmt.setString(4, reiziger.getAchternaam());
            stmt.setDate(5, reiziger.getGeboortedatum());
            stmt.executeUpdate();
        }
    }

    @Override
    public Reiziger read(int id) throws SQLException {
        String sql = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum")
                );
            }
        }
        return null;
    }

    @Override
    public void update(Reiziger reiziger) throws SQLException {
        String sql = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reiziger.getVoorletters());
            stmt.setString(2, reiziger.getTussenvoegsel());
            stmt.setString(3, reiziger.getAchternaam());
            stmt.setDate(4, reiziger.getGeboortedatum());
            stmt.setInt(5, reiziger.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();
        String sql = "SELECT * FROM reiziger";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reizigers.add(new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum")
                ));
            }
        }
        return reizigers;
    }
}
