package DAO;
import java.sql.SQLException;
import java.util.List;
import hu.nl.ovchip.domein.Reiziger;

public interface ReizigerDAO {
    void create(Reiziger reiziger) throws SQLException;
    Reiziger read(int id) throws SQLException;
    void update(Reiziger reiziger) throws SQLException;
    void delete(int id) throws SQLException;
    List<Reiziger> findAll() throws SQLException;
}