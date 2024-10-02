import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import dao.data.*;
import model.domein.domain.Reiziger;

import java.sql.Date;

public class Main {
    private static final String URL = "jdbc:postgresql://localhost:5432/ovchip";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Yildirim112";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            ReizigerDAO rdao = new ReizigerDAOPsql(connection);
            testReizigerDAO(rdao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");
        

        // 1. Test findAll
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // 2. Test save
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum));
        System.out.println("[Test] Eerst " + reizigers.size() + " reizigers.");
        rdao.save(sietske); 
        reizigers = rdao.findAll();
        System.out.println("[Test] Na ReizigerDAO.save() " + reizigers.size() + " reizigers.");
        System.out.println("Toegevoegde reiziger: " + rdao.findById(77)); // Verify by reading the added Reiziger
        System.out.println();

        

        // 3. Test update
        sietske.setAchternaam("De Vries");
        rdao.update(sietske);
        List<Reiziger> reizigersList = rdao.findByGbdatum(Date.valueOf(gbdatum));
        
        System.out.println("[Test] Na ReizigerDAO.update() gewijzigd naar: " + reizigersList);
        System.out.println();

        // 4. Test delete
        rdao.delete(77);
        reizigers = rdao.findAll();
        System.out.println("[Test] Na ReizigerDAO.delete() " + reizigers.size() + " reizigers.");
        System.out.println("Verwijderde reiziger met ID 77: " + rdao.findById(77)); // Should be null or indicate deletion
        System.out.println();
    }
}