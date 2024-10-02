import DAO.ReizigerDAO;
import DAO.ReizigerDAOHibernate;
import domain.Reiziger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.sql.SQLException;

public class Main {
    // Create an EntityManagerFactory when the application is started
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ovchipkaartPU");

    public static void main(String[] args) throws SQLException {
        EntityManager em = emf.createEntityManager();

        // Create DAO's
        ReizigerDAO reizigerDAO = new ReizigerDAOHibernate(em);

        // Test DAO's
        testReizigerDAO(reizigerDAO);

        em.close();
        emf.close();
    }

    // Test the ReizigerDAO
    public static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        // Get all reizigers from the database
        System.out.println("---- Test ReizigerDAO ----");
        System.out.println("Alle reizigers: ");
        for (Reiziger r : rdao.findAll()) {
            System.out.println(r);
        }
        System.out.println();

        // Get reiziger with id 2
        System.out.println("Reiziger met id 2: ");
        System.out.println(rdao.findById(2));
        System.out.println();

        // Get reizigers with birthdate 2002-10-22
        System.out.println("Reizigers met geboortedatum 2002-10-22: ");
        for (Reiziger r : rdao.findByGbdatum(java.sql.Date.valueOf("2002-10-22"))) {
            System.out.println(r);
        }
        System.out.println();

        // Create a new reiziger and persist it in the database
        Reiziger r = new Reiziger(6, "HAJ", "de", "bam", java.sql.Date.valueOf("2004-02-23"));
        System.out.print("Reiziger Kenan toevoegen: ");
        if (rdao.save(r)) {

            System.out.println("Gelukt!");
            System.out.println("Reiziger: " + r);
        } else {
            System.out.println("Mislukt!");
        }
        System.out.println();

        // Update the new reiziger in the database
        r.setAchternaam("Groot");
        System.out.print("Reiziger  updaten: ");
        if (rdao.update(r)) {
            System.out.println("Gelukt!");
            System.out.println("Reiziger : " + r);
        } else {
            System.out.println("Mislukt!");
        }
        System.out.println();

        // Delete the new reiziger from the database
        System.out.print("Reiziger verwijderen: ");
        if (rdao.delete(r)) {
            System.out.println("Gelukt!");
        } else {
            System.out.println("Mislukt!");
        }
        System.out.println();
    }
}