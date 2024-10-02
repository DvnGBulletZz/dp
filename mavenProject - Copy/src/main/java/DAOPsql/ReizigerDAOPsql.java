package DAOPsql;

// import dao.data.AdresDAO;
import DAO.AdresDAO;
import DAO.ReizigerDAO;
import domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {

    private Connection conn;


    public ReizigerDAOPsql(Connection conn, AdresDAO adresDAO) {
        this.conn = conn;


    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        String query = """
                INSERT INTO reiziger (
                reiziger_id,
                voorletters,
                tussenvoegsel,
                achternaam,
                geboortedatum)
                VALUES (?, ?, ?, ?, ?);
           """;


        PreparedStatement statement = conn.prepareStatement(query);

        statement.setInt(1, reiziger.getId());
        statement.setString(2, reiziger.getVoorletters());
        statement.setString(3, reiziger.getTussenvoegsel());
        statement.setString(4, reiziger.getAchternaam());
        statement.setDate(5, (java.sql.Date)reiziger.getGeboortedatum());

        statement.executeUpdate();
        statement.close();




         return true;
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        String query =

                "UPDATE reiziger "+
                "SET"+
                "voorletters= ?,"+
                "tussenvoegsel=?,"+
                "achternaam = ?,"+
                "geboortedatum = ?"+
                "WHERE reiziger_id = ?;";

        PreparedStatement statement = conn.prepareStatement(query);

        statement.setString(1, reiziger.getVoorletters());
        statement.setString(2, reiziger.getTussenvoegsel());
        statement.setString(3, reiziger.getAchternaam());
        statement.setDate(4, (java.sql.Date)reiziger.getGeboortedatum());
        statement.setInt(5, reiziger.getId());

        statement.executeUpdate();
        statement.close();



        return true;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {


        String query =
                "DELETE FROM reiziger " +
                "WHERE reiziger_id = ?;";
        PreparedStatement statement = conn.prepareStatement(query);

        statement.setInt(1, reiziger.getId());

        statement.executeUpdate();
        statement.close();

        return true;
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        Reiziger reiziger = new Reiziger();

        String query =
            "SELECT reiziger.*, adres.adres_id\n" +
            "FROM reiziger\n" +
            "WHERE reiziger.reiziger_id = ?;";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            reiziger.setId(rs.getInt(1));
            reiziger.setVoorletters(rs.getString(2));
            reiziger.setTussenvoegsel(rs.getString(3));
            reiziger.setAchternaam(rs.getString(4));
            reiziger.setGeboortedatum(rs.getDate(5));
        }

        rs.close();
        statement.close();

        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) throws SQLException {
        List<Reiziger> reizigerList = new ArrayList<>();

        String query =
            "SELECT reiziger.*, adres.adres_id" +
            "FROM reiziger" +
            "INNER JOIN adres" +
            "ON adres.reiziger_id = reiziger.reiziger_id" +
            "WHERE geboortedatum = ?;";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.setDate(1, date);
        ResultSet rs = statement.executeQuery();

        Reiziger reiziger = new Reiziger();
        int oldId = -1;

        while (rs.next()) {
            int currentId = rs.getInt(1);

            if(oldId != currentId) {
                reiziger.setId(rs.getInt(1));
                reiziger.setVoorletters(rs.getString(2));
                reiziger.setTussenvoegsel(rs.getString(3));
                reiziger.setAchternaam(rs.getString(4));
                reiziger.setGeboortedatum(rs.getDate(5));


                reizigerList.add(reiziger);
                oldId = currentId;
            } 
        }

        rs.close();
        statement.close();

        return reizigerList;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        List<Reiziger> reizigerList = new ArrayList<>();

        String query =
            "SELECT reiziger.*, adres.adres_id\n" +
            "FROM reiziger\n" +
            "ORDER BY reiziger.reiziger_id ASC";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
            
        Reiziger reiziger = new Reiziger();

        int oldId = -1;

        while (rs.next()) {
            int currentId = rs.getInt(1);

            if (oldId != currentId) {
                reiziger = new Reiziger();
                reiziger.setId(rs.getInt(1));
                reiziger.setVoorletters(rs.getString(2));
                reiziger.setTussenvoegsel(rs.getString(3));
                reiziger.setAchternaam(rs.getString(4));
                reiziger.setGeboortedatum(rs.getDate(5));


                reizigerList.add(reiziger);
                oldId = currentId;
            }
        }

        rs.close();
        statement.close();

        return reizigerList;
    }
}

