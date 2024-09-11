package dao.data;
import java.sql.SQLException;
import java.util.List;
import java.sql.Date;

import model.domein.domain.Reiziger;

public interface ReizigerDAO {
    void save(Reiziger reiziger) throws SQLException;
    Reiziger findById(int id) throws SQLException;
    Reiziger findByGbdatum(Date gbDatum) throws SQLException;
    void update(Reiziger reiziger) throws SQLException;
    void delete(int id) throws SQLException;
    List<Reiziger> findAll() throws SQLException;
}