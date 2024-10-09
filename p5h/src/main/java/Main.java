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
import java.util.ArrayList;
import java.util.List;
import DAO.ProductDAO;
import DAOPsql.ProductDAOPsql;
import domain.Product;

public class Main {
    // remove dit ik gebruik dit als een eigen class
    static ConfigReader configReader = new ConfigReader();
    static String URL = configReader.getDatabaseURL();
    static String USER = configReader.getDatabaseUsername();
    static String PASSWORD = configReader.getDatabasePassword();
    // Create a SessionFactory when the application is started
    private static SessionFactory sessionFactory;

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello world");

//        try {
//
//            sessionFactory = new Configuration().configure().buildSessionFactory();
//
//            Session session = sessionFactory.openSession();
//
//            // Create DAO's
//            AdresDAO adresDAO = new AdresDAOHibernate(session);
//            ProductDAO productDAO = new ProductDAOHibernate(session);
//            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOHibernate(session);
//            ReizigerDAO reizigerDAO = new ReizigerDAOHibernate(session, adresDAO, ovChipkaartDAO);
//
//            System.out.println("---- Test DAO's ----");
//            // Test DAO's
//            testDAOs(reizigerDAO, ovChipkaartDAO, productDAO);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (sessionFactory != null) {
//                sessionFactory.close();  // Always close session after work is done
//            }
//            // Do not close sessionFactory here if your app is not finished
//            // sessionFactory.close();
//        }



        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            AdresDAO adresDAO = new AdresDAOPsql(connection);
            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOPsql(connection);
            ReizigerDAO reizigerDAO = new ReizigerDAOPsql(connection, adresDAO, ovChipkaartDAO);
            ovChipkaartDAO.setReizigerDAO(reizigerDAO);
            adresDAO.setReizigerDAO(reizigerDAO);
            ProductDAO productDAO = new ProductDAOPsql(connection);
            ovChipkaartDAO.setProductDAO(productDAO);

            testDAOs(reizigerDAO, ovChipkaartDAO, productDAO);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    // Combined test for ReizigerDAO and OVChipkaartDAO
    public static void testDAOs(ReizigerDAO rdao, OVChipkaartDAO odao, ProductDAO pdao) throws SQLException {


        // Test ReizigerDAO
        System.out.println("---- Test ReizigerDAO ----");

        System.out.println("Alle reizigers: ");
        List<Reiziger> reizigers = rdao.findAll();
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        System.out.println("Reizigers met geboortedatum 2002-10-22: ");
        List<Reiziger> reizigersByDate = rdao.findByGbdatum(java.sql.Date.valueOf("2002-10-22"));
        for (Reiziger r : reizigersByDate) {
            System.out.println(r);
        }
        System.out.println();

        // Adding a new Reiziger, Adres, OVChipkaart, and Product
        Adres adres = new Adres();
        adres.setId(7);
        adres.setPostcode("1234AB");
        adres.setHuisnummer("4");
        adres.setStraat("Majan");
        adres.setWoonplaats("Plakcentrum");

        OVChipkaart ovChipkaart = new OVChipkaart();
        ovChipkaart.setKaartNummer(98989898);
        ovChipkaart.setGeldigTot(java.sql.Date.valueOf("2025-12-31"));
        ovChipkaart.setKlasse(2);
        ovChipkaart.setSaldo(50.0);

        Product product = new Product();
        product.setProductNummer(5678);
        product.setProductNaam("Travel Card");
        product.setBeschrijving("Unlimited travel in the Netherlands");
        product.setPrijs(99.99);



        // Step 2: Save the Reiziger (which cascades the OVChipkaart and its associations)
        Reiziger reiziger = new Reiziger();
        reiziger.setId(7);
        reiziger.setVoorletters("M");
        reiziger.setTussenvoegsel(null);
        reiziger.setAchternaam("Bakfiets");
        reiziger.setGeboortedatum(java.sql.Date.valueOf("2002-02-25"));
        reiziger.setAdres(adres);
        reiziger.setOvChipkaarten(List.of(ovChipkaart));

        adres.setReiziger(reiziger);
        ovChipkaart.setReiziger(reiziger);
        pdao.delete(product);
        // ---- DELETE OPERATION ----
        System.out.print("\nReiziger verwijderen: ");
        if (rdao.delete(reiziger)) {
            System.out.println("Gelukt!");
        } else {
            System.out.println("Mislukt!");
        }

        // Delete the OVChipkaart-Product relation
        System.out.print("\nRelatie tussen OVChipkaart en Product verwijderen: ");
        if (odao.delete(ovChipkaart)) {  // Assuming `deleteProductRelation` exists in `OVChipkaartDAO`
            System.out.println("Relatie verwijderd.");
        } else {
            System.out.println("Mislukt!");
        }

        // Step 1: Save the product first
        System.out.print("Product toevoegen: ");
        if (pdao.save(product)) {
            System.out.println("Gelukt!");
            System.out.println("Product: " + product);
        } else {
            System.out.println("Mislukt!");
        }


        // ---- MANAGING OVCHIPKAART_PRODUCT RELATIONSHIP ----
        System.out.println("\n---- Manage OVChipkaart_Product ----");

        // Add a product to the OVChipkaart (The relation is saved automatically now)
        ovChipkaart.addProduct(product);  // Set the bidirectional relationship




        System.out.print("Reiziger toevoegen: ");
        if (rdao.save(reiziger)) {
            System.out.println("Gelukt!");
            System.out.println("Reiziger: " + reiziger);
        } else {
            System.out.println("Mislukt!");
        }
        // Save the OVChipkaart along with its Product relation
        System.out.print("OVChipkaart opslaan inclusief Product-relatie: ");
        if (odao.save(ovChipkaart)) {  // This will automatically save the relation in the `ov_chipkaart_product` table
            System.out.println("Gelukt! OVChipkaart en Product-relatie toegevoegd.");
        } else {
            System.out.println("Mislukt!");
        }





        // ---- RETRIEVING PRODUCTS ASSOCIATED WITH AN OVCHIPKAART ----
        System.out.println("\n---- Retrieve Products by OVChipkaart ----");

        List<Product> productsByOV = pdao.findByOVChipkaart(ovChipkaart);  // Find products by OVChipkaart
        System.out.println("Producten gekoppeld aan OVChipkaart: ");
        for (Product p : productsByOV) {
            System.out.println(p);
        }

        // ---- UPDATE OPERATION ----
        System.out.println("\n---- Update Entities ----");

        // Update Product
        product.setPrijs(120.00); // Change product price
        System.out.print("Product bijwerken: ");
        if (pdao.update(product)) {
            System.out.println("Gelukt!");
            System.out.println("Updated Product: " + product);
        } else {
            System.out.println("Mislukt!");
        }

        // Update OVChipkaart
        ovChipkaart.setSaldo(75.0); // Update OVChipkaart saldo
        System.out.print("OVChipkaart bijwerken: ");
        if (odao.update(ovChipkaart)) {
            System.out.println("Gelukt!");
            System.out.println("Updated OVChipkaart: " + ovChipkaart);
        } else {
            System.out.println("Mislukt!");
        }

        // Update Reiziger
        reiziger.setAchternaam("Fietsman"); // Update Reiziger achternaam
        System.out.print("Reiziger bijwerken: ");
        if (rdao.update(reiziger)) {
            System.out.println("Gelukt!");
            System.out.println("Updated Reiziger: " + reiziger);
        } else {
            System.out.println("Mislukt!");
        }

        // ---- DELETE OPERATION ----


        // ---- DELETE PRODUCT-OVCHIPKAART ASSOCIATIONS ----
        System.out.print("\nRelatie tussen OVChipkaart en Product verwijderen: ");
        List<Product> products = new ArrayList<>(ovChipkaart.getProducts());
        for (Product prd : products) {
            ovChipkaart.removeProduct(prd);  // Break the relationship on the OVChipkaart side
            prd.removeOVChipkaart(ovChipkaart);  // Break the relationship on the Product side
            pdao.update(prd);  // Update the Product in the database to persist the removal
        }

        // Delete Product
        System.out.print("\nProduct verwijderen: ");
        if (pdao.delete(product)) {
            System.out.println("Product verwijderd.");
        } else {
            System.out.println("Mislukt om Product te verwijderen.");
        }

        System.out.print("\nReiziger verwijderen: ");
        if (rdao.delete(reiziger)) {
            System.out.println("Reiziger succesvol verwijderd.");
        } else {
            System.out.println("Mislukt om reiziger te verwijderen.");
        }
    }

}
