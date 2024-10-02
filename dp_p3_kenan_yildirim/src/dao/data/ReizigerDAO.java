package dao.data;
import java.sql.SQLException;
import java.util.List;
import java.sql.Date;

import model.domein.domain.Reiziger;

public interface ReizigerDAO {
    boolean save(Reiziger reiziger) throws SQLException;
    Reiziger findById(int id) throws SQLException;
    List<Reiziger> findByGbdatum(Date gbDatum) throws SQLException;
    boolean update(Reiziger reiziger) throws SQLException;
    boolean delete(Reiziger reiziger) throws SQLException;
    List<Reiziger> findAll() throws SQLException;
}