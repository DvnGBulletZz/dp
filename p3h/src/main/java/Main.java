import DAO.ReizigerDAO;
import DAOPsql.*;
import domain.Reiziger;
import domain.Adres;
import DAO.AdresDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class Main {

    // comment out the following line to test the DAOs with a connection to the database without hibernate
    static ConfigReader configReader = new ConfigReader();
    static String URL = configReader.getDatabaseURL();
    static String USER = configReader.getDatabaseUsername();
    static String PASSWORD = configReader.getDatabasePassword();
    // Create a SessionFactory when the application is started
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello world");
        Session session = sessionFactory.openSession();

        // Create DAO's
        AdresDAO adresDAO = new AdresDAOHibernate(session);

        ReizigerDAO reizigerDAO = new ReizigerDAOHibernate(session, adresDAO);
        adresDAO.setReizigerDAO(reizigerDAO);
        // Test DAO's
        testDAOs(reizigerDAO, adresDAO);

        session.close();
        sessionFactory.close();


// uncomment this code to test the DAOs with a connection to the database without hibernate
//        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
//            // Create DAO instances
//            AdresDAO adresDAO = new AdresDAOPsql(connection);
//
//            ReizigerDAO reizigerDAO = new ReizigerDAOPsql(connection, adresDAO);
//
//            adresDAO.setReizigerDAO(reizigerDAO);
//            // Set the ReizigerDAO for OVChipkaartDAO
//
//            testDAOs(reizigerDAO, adresDAO);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


    }

    // Combined test for ReizigerDAO and OVChipkaartDAO
    public static void testDAOs(ReizigerDAO rdao, AdresDAO adao) throws SQLException {
        // Test ReizigerDAO
        System.out.println("---- Test ReizigerDAO ----");
        System.out.println("Alle reizigers: ");
        for (Reiziger r : rdao.findAll()) {
            System.out.println(r);
        }
        System.out.println();

        System.out.println("Reizigers met geboortedatum 2002-10-22: ");
        for (Reiziger r : rdao.findByGbdatum(java.sql.Date.valueOf("2002-10-22"))) {
            System.out.println(r);
        }
        System.out.println();

        Adres adres = new Adres();
        adres.setId(7);
        adres.setPostcode("1234AB");
        adres.setHuisnummer("4");
        adres.setStraat("majan");
        adres.setWoonplaats("plakcentrum");




        Reiziger reiziger = new Reiziger();
        reiziger.setId(7);
        reiziger.setVoorletters("m");
        reiziger.setTussenvoegsel(null);
        reiziger.setAchternaam("bakfiets");
        reiziger.setGeboortedatum(java.sql.Date.valueOf("2002-02-25"));
        reiziger.setAdres(adres);

        adres.setReiziger(reiziger);




        System.out.print("Reiziger Jameson toevoegen: ");
        if (rdao.save(reiziger)) {
            System.out.println("Gelukt!");
            System.out.println("Reiziger Jameson: " + reiziger);
        } else {
            System.out.println("Mislukt!");
        }
        System.out.println();

        System.out.println("Reiziger met id 7: ");
        System.out.println(rdao.findById(7));
        System.out.println();

        reiziger.setAchternaam("RIBEYE");
        System.out.print("Reiziger Jameson updaten: ");
        if (rdao.update(reiziger)) {
            System.out.println("Gelukt!");
            System.out.println("Reiziger Jameson: " + reiziger);
        } else {
            System.out.println("Mislukt!");
        }
        System.out.println();

        System.out.println("adres met id 7: ");
        System.out.println(adao.findById(7));
        System.out.println();

        System.out.println("vind alle adressen ");
        System.out.println(adao.findAll());
        System.out.println();


        System.out.println("find by reiziger");
        System.out.println(adao.findByReiziger(reiziger));
        System.out.println();



        System.out.print("Reiziger Jameson verwijderen: ");
        if (rdao.delete(reiziger)) {
            System.out.println("Gelukt!");
        } else {
            System.out.println("Mislukt!");
        }
        System.out.println();
    }
}