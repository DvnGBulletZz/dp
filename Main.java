import DAO.ReizigerDAO;
import DAOPsql.ReizigerDAOHibernate;
import domain.Reiziger;
import domain.Adres;
import DAOPsql.AdresDAOHibernate;
import DAO.AdresDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.SQLException;

public class Main {
    // Create a SessionFactory when the application is started
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello world");
        Session session = sessionFactory.openSession();

        // Create DAO's
        AdresDAO adresDAO = new AdresDAOHibernate(session);
        ReizigerDAO reizigerDAO = new ReizigerDAOHibernate(session, adresDAO);

        // Test DAO's
        testReizigerDAO(reizigerDAO, session);

        session.close();
        sessionFactory.close();
    }

    // Test the ReizigerDAO
    public static void testReizigerDAO(ReizigerDAO rdao, Session session) throws SQLException {
        // Get all reizigers from the database
        System.out.println("---- Test ReizigerDAO ----");
        System.out.println("Alle reizigers: ");
        for (Reiziger r : rdao.findAll()) {
            System.out.println(r);
        }
        System.out.println();

        // Get reizigers with birthdate 2002-10-22
        System.out.println("Reizigers met geboortedatum 2002-10-22: ");
        for (Reiziger r : rdao.findByGbdatum(java.sql.Date.valueOf("2002-10-22"))) {
            System.out.println(r);
        }
        System.out.println();

        // Create a new reiziger and persist it in the database
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

        adres.setReiziger(reiziger); // Set the Reiziger reference in the Adres object

        // Start a transaction
        session.beginTransaction();


            // Save the new Reiziger
        System.out.print("Reiziger Jameson toevoegen: ");
        if (rdao.save(reiziger)) {
            System.out.println("Gelukt!");
            System.out.println("Reiziger Jameson: " + reiziger);
        } else {
            System.out.println("Mislukt!");
        }
        System.out.println();

        // Commit the transaction
//        session.getTransaction().commit();


        // Get reiziger with id 2
        System.out.println("Reiziger met id 7: ");
        System.out.println(rdao.findById(1));
        System.out.println();

        // Update the new reiziger in the database
        reiziger.setAchternaam("RIBEYE");
        System.out.print("Reiziger Jameson updaten: ");
        if (rdao.update(reiziger)) {
            System.out.println("Gelukt!");
            System.out.println("Reiziger Jameson: " + reiziger);
        } else {
            System.out.println("Mislukt!");
        }
        System.out.println();

        // Delete the new reiziger from the database
        System.out.print("Reiziger Jameson verwijderen: ");
        if (rdao.delete(reiziger)) {
            System.out.println("Gelukt!");
        } else {
            System.out.println("Mislukt!");
        }
        System.out.println();
    }
}