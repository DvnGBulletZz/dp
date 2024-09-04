public class Main {
    private static final String URL = "jdbc:postgresql://localhost:5432/ovchip";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Yildirim112";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            ReizigerDAO dao = new ReizigerDAOPsql(connection);
            testReizigerDAO(dao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void testReizigerDAO(ReizigerDAO dao) {
        
    }
}