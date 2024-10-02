import DAO.ReizigerDAO;
import DAOPsql.ReizigerDAOPsql;
import domain.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        String user = "postgres";
        String password = "Yildirim112";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            ReizigerDAO reizigerDAO = new ReizigerDAOPsql(conn);


            // Test all DAOs
            testAlles(reizigerDAO);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testAlles(ReizigerDAO reizigerDAO) throws SQLException {
        System.out.println("---- Test Alles ----");

        // Create a new Reiziger
        Reiziger reiziger1 = new Reiziger();
        reiziger1.setId(10);
        reiziger1.setVoorletters("J");
        reiziger1.setTussenvoegsel("van");
        reiziger1.setAchternaam("Frikandel");
        reiziger1.setGeboortedatum(Date.valueOf("1990-01-01"));

        // Save the Reiziger
        System.out.println("reiziger1 created: " + reiziger1);



        reizigerDAO.save(reiziger1);

        // Find all Reizigers
        List<Reiziger> reizigers = reizigerDAO.findAll();
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }


        // Update the Reiziger
        reiziger1.setAchternaam("Smith");
        reizigerDAO.update(reiziger1);
        System.out.println("updated reiziger: " + reiziger1);


        // Find by ID
        Reiziger foundReiziger = reizigerDAO.findById(10);
        System.out.println("reiziger found " + foundReiziger);



        // Delete the Reiziger
        reizigerDAO.delete(reiziger1);
        System.out.println("deleted reiziger: " + reiziger1);
    }
}