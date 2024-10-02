package DAOPsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.AdresDAO;
import domain.Adres;


public class AdresDAOPsql implements AdresDAO {
    private Connection connection;


    public AdresDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Adres adres) throws SQLException {
        String sql = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, adres.getId());
            stmt.setString(2, adres.getPostcode());
            stmt.setString(3, adres.getHuisnummer());
            stmt.setString(4, adres.getStraat());
            stmt.setString(5, adres.getWoonplaats());
            stmt.setInt(6, adres.getReizigerId());  // FK to Reiziger (use ID here)
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Adres adres) throws SQLException {
        String sql = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ? WHERE adres_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, adres.getPostcode());
            stmt.setString(2, adres.getHuisnummer());
            stmt.setString(3, adres.getStraat());
            stmt.setString(4, adres.getWoonplaats());
            stmt.setInt(5, adres.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Adres adres) throws SQLException {
        String sql = "DELETE FROM adres WHERE adres_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, adres.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public Adres findByReizigerId(int reizigerId) throws SQLException {
        String sql = 
        "SELECT * FROM adres"+
        "WHERE reiziger_id = ?"+
        "INNER JOIN reiziger ON adres.reiziger_id = reiziger.reiziger_id"+
        "WHERE reiziger.reiziger_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reizigerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Adres(
                    rs.getInt("adres_id"),
                    rs.getString("postcode"),
                    rs.getString("huisnummer"),
                    rs.getString("straat"),
                    rs.getString("woonplaats"),
                    rs.getInt("reiziger_id")  // Retrieve the Reiziger ID
                );
                
            }
            rs.close();
            stmt.close();
        }
        return null;
    }

    @Override
    public Adres findById(int adresId) throws SQLException {
        String sql = "SELECT * FROM adres WHERE adres_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, adresId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Adres(
                    rs.getInt("adres_id"),
                    rs.getString("postcode"),
                    rs.getString("huisnummer"),
                    rs.getString("straat"),
                    rs.getString("woonplaats"),
                    rs.getInt("reiziger_id")  // Retrieve the Reiziger ID
                );
            }
        }
        return null;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        String sql = "SELECT * FROM adres";
        List<Adres> adressen = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {

                // Optionally retrieve Reiziger if needed
                // Reiziger reiziger = reizigerDAO.findById(reizigerId);
                
                Adres adres = new Adres(
                    rs.getInt("adres_id"),
                    rs.getString("postcode"),
                    rs.getString("huisnummer"),
                    rs.getString("straat"),
                    rs.getString("woonplaats"),
                    rs.getInt("reiziger_id")  // Retrieve the Reiziger ID
                );
                
                adressen.add(adres);
            }
            rs.close();
            stmt.close();
        }
       
        return adressen;
    }
}
