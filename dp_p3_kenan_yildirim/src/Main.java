

import dao.data.ReizigerDAOPsql;
import dao.data.AdresDAOPsql;
import dao.data.ReizigerDAO;
import dao.data.AdresDAO;
import model.domein.domain.Reiziger;
import model.domein.domain.Adres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;


public class Main {
    static ConfigReader configReader = new ConfigReader();
    static String URL = configReader.getDatabaseURL();
    static String USER = configReader.getDatabaseUsername();
    static String PASSWORD = configReader.getDatabasePassword();


    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Create DAO instances
            AdresDAO adresDAO = new AdresDAOPsql(connection); // Set ReizigerDAO to null or mock as needed
            ReizigerDAO reizigerDAO = new ReizigerDAOPsql(connection, adresDAO);

            // Run tests
            // System.out.println("hallo ik ben hier");
            testReizigerAndAdres(reizigerDAO, adresDAO);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void testReizigerAndAdres(ReizigerDAO reizigerDAO, AdresDAO adresDAO) {
        // Example test code to demonstrate functionality
        try {
            // Create a test Reiziger and Adres
            Reiziger reiziger = new Reiziger(10, "K", "", "YILDIRIM", Date.valueOf("2000-01-01"), null);

            Adres adres = new Adres(10, "1234AB", "10", "DUIFHUIS", "ZEIST", reiziger.getId()); // No Reiziger yet
            reiziger.setAdres(adres); // Set the Adres

            // Save the Reiziger
            reizigerDAO.save(reiziger);

            // Fetch and print the Reiziger and associated Adres
            Reiziger fetchedReiziger = reizigerDAO.findById(10);
            System.out.println(fetchedReiziger);

            // Fetch and print all Adres records
            List<Adres> allAdressen = adresDAO.findAll();
            for (Adres addr : allAdressen) {
                System.out.println(addr);
            }

            // delete reiziger
            reizigerDAO.delete(reiziger);
            System.out.println("Deleted reiziger: " + reiziger);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}