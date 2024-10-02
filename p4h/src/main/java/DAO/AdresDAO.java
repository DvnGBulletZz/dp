package DAO;

// import java.sql.ResultSet;
import java.sql.SQLException;
// import java.util.ArrayList;
import java.util.List;
import domain.Adres;
import domain.Reiziger;

public interface AdresDAO {
    void save(Adres adres) throws SQLException;
    void update(Adres adres) throws SQLException;
    void delete(Adres adres) throws SQLException;
    Adres findByReiziger(Reiziger reiziger) throws SQLException;
    Adres findById(int adresId) throws SQLException;
    List<Adres> findAll() throws SQLException;
    void setReizigerDAO(ReizigerDAO rDao);

}