package dao.data;

// import java.sql.ResultSet;
import java.sql.SQLException;
// import java.util.ArrayList;
import java.util.List;
import model.domein.domain.Adres;

public interface AdresDAO {
    void save(Adres adres) throws SQLException;
    void update(Adres adres) throws SQLException;
    void delete(Adres adres) throws SQLException;
    Adres findByReizigerId(int reizigerId) throws SQLException;
    Adres findById(int adresId) throws SQLException;
    List<Adres> findAll() throws SQLException;

}