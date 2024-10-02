package DAOPsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import domain.Adres;
import DAO.AdresDAO;
import domain.Reiziger;
import DAO.ReizigerDAO;

public class AdresDAOPsql implements AdresDAO {
    private Connection connection;
    private ReizigerDAOPsql rDao;

    public AdresDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Adres adres) {
        String sql = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, adres.getId());
            stmt.setString(2, adres.getPostcode());
            stmt.setString(3, adres.getHuisnummer());
            stmt.setString(4, adres.getStraat());
            stmt.setString(5, adres.getWoonplaats());
            stmt.setInt(6, adres.getReiziger().getId());  // FK to Reiziger (use ID here)
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Adres adres) {
        String sql = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ? WHERE adres_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, adres.getPostcode());
            stmt.setString(2, adres.getHuisnummer());
            stmt.setString(3, adres.getStraat());
            stmt.setString(4, adres.getWoonplaats());
            stmt.setInt(5, adres.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Adres adres) {
        String sql = "DELETE FROM adres WHERE adres_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, adres.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        String sql = "SELECT * FROM adres WHERE reiziger_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reiziger.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Adres(
                            rs.getInt("adres_id"),
                            rs.getString("postcode"),
                            rs.getString("huisnummer"),
                            rs.getString("straat"),
                            rs.getString("woonplaats"),
                            reiziger  // Pass the Reiziger object instead of ID
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Adres findById(int adresId) {
        String sql = "SELECT * FROM adres WHERE adres_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, adresId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int reizigerId = rs.getInt("reiziger_id");
                    return new Adres(
                            rs.getInt("adres_id"),
                            rs.getString("postcode"),
                            rs.getString("huisnummer"),
                            rs.getString("straat"),
                            rs.getString("woonplaats"),
                            rDao.findById(reizigerId)  // Use rDao to get Reiziger object by ID
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Adres> findAll() {
        String sql = "SELECT * FROM adres";
        List<Adres> adressen = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int reizigerId = rs.getInt("reiziger_id");
                Adres adres = new Adres(
                        rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        rDao.findById(reizigerId)  // Pass the Reiziger object
                );
                adressen.add(adres);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adressen;
    }

    @Override
    public void setReizigerDAO(ReizigerDAO rDao) {
        this.rDao = (ReizigerDAOPsql) rDao;
    }
}