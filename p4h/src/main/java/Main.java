import DAO.ReizigerDAO;
import DAO.OVChipkaartDAO;
import DAOPsql.*;
import domain.Reiziger;
import domain.Adres;
import domain.OVChipkaart;
import DAO.AdresDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class Main {
    // remove dit ik gebruik dit als een eigen class
    static ConfigReader configReader = new ConfigReader();
    static String URL = configReader.getDatabaseURL();
    static String USER = configReader.getDatabaseUsername();
    static String PASSWORD = configReader.getDatabasePassword();
    // Create a SessionFactory when the application is started
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello world");
//        Session session = sessionFactory.openSession();
//
//        // Create DAO's
//        AdresDAO adresDAO = new AdresDAOhibernate(session);
//        OVChipkaartDAO ovChipkaartDAO = new OVChipkaarthibernate(session);
//        ReizigerDAO reizigerDAO = new ReizigerDAOHibernate(session, adresDAO, ovChipkaartDAO);
//
//        // Test DAO's
//        testDAOs(reizigerDAO, ovChipkaartDAO, session);
//
//        session.close();
//        sessionFactory.close();
//


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Create DAO instances
            AdresDAO adresDAO = new AdresDAOPsql(connection);
            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOPsql(connection);
            ReizigerDAO reizigerDAO = new ReizigerDAOPsql(connection, adresDAO, ovChipkaartDAO);
            ovChipkaartDAO.setReizigerDAO(reizigerDAO);
            adresDAO.setReizigerDAO(reizigerDAO);
            // Set the ReizigerDAO for OVChipkaartDAO

            testDAOs(reizigerDAO, ovChipkaartDAO);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    // Combined test for ReizigerDAO and OVChipkaartDAO
    public static void testDAOs(ReizigerDAO rdao, OVChipkaartDAO odao) throws SQLException {
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



        OVChipkaart ovChipkaart = new OVChipkaart();
        ovChipkaart.setKaartNummer(12345);
        ovChipkaart.setGeldigTot(java.sql.Date.valueOf("2025-12-31"));
        ovChipkaart.setKlasse(2);
        ovChipkaart.setSaldo(50.0);



        Reiziger reiziger = new Reiziger();
        reiziger.setId(7);
        reiziger.setVoorletters("m");
        reiziger.setTussenvoegsel(null);
        reiziger.setAchternaam("bakfiets");
        reiziger.setGeboortedatum(java.sql.Date.valueOf("2002-02-25"));
        reiziger.setAdres(adres);
        reiziger.addOvChipkaart(ovChipkaart);

        adres.setReiziger(reiziger);
        ovChipkaart.setReiziger(reiziger);



        System.out.print("Reiziger Jameson toevoegen: ");
        if (rdao.save(reiziger)) {
            System.out.println("Gelukt!");
            System.out.println("Reiziger Jameson: " + reiziger);
        } else {
            System.out.println("Mislukt!");
        }
        System.out.println();

        System.out.println("Reiziger met id 7: ");
        System.out.println(rdao.findById(1));
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



        // Test OVChipkaartDAO
        System.out.println("---- Test OVChipkaartDAO ----");
        System.out.println("Alle OVChipkaarten: ");
        for (OVChipkaart o : odao.findAll()) {
            System.out.println(o);
        }
        System.out.println();


        System.out.println("OVChipkaart met id 12345: ");
        System.out.println(odao.findById(12345));
        System.out.println();

        ovChipkaart.setSaldo(75.0);
        System.out.print("OVChipkaart updaten: ");
        if (odao.update(ovChipkaart)) {
            System.out.println("Gelukt!");
            System.out.println("OVChipkaart: " + ovChipkaart);
        } else {
            System.out.println("Mislukt!");
        }
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