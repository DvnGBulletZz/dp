package DAOPsql;


import DAO.OVChipkaartDAO;
import DAO.ProductDAO;
import DAO.ReizigerDAO;
import domain.Adres;
import domain.OVChipkaart;

import domain.Reiziger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.sql.Date;
import java.util.List;
import org.hibernate.query.Query;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {
    private final Session session;


    public OVChipkaartDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        Transaction tx = null;

        try {
            if (session.getTransaction().isActive()) {
                System.out.println("Transaction is already active.");
            } else {
                System.out.println("Transaction is not active. Starting a new transaction.");
                tx = session.beginTransaction();
            }
            session.persist(ovChipkaart);
            if (tx != null) {
                tx.commit();
            }
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        Transaction tx = null;
        try {
            if (session.getTransaction().isActive()) {
                System.out.println("Transaction is already active.");
            } else {
                System.out.println("Transaction is not active. Starting a new transaction.");
                tx = session.beginTransaction();
            }
            session.merge(ovChipkaart);
            if (tx != null) {
                tx.commit();
            }
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        Transaction tx = null;
        try {
            if (session.getTransaction().isActive()) {
                System.out.println("Transaction is already active.");
            } else {
                System.out.println("Transaction is not active. Starting a new transaction.");
                tx = session.beginTransaction();
            }

            session.remove(ovChipkaart);
            if (tx != null) {
                tx.commit();
            }
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        Query query = session.createQuery("FROM OVChipkaart WHERE reiziger = :reiziger");
        query.setParameter("reiziger_id", reiziger.getId());
        return query.list();
    }

    @Override
    public OVChipkaart findById(int id) {
        return session.get(OVChipkaart.class, id);
    }

    @Override
    public List<OVChipkaart> findAll() {
        Query query = session.createQuery("FROM OVChipkaart", OVChipkaart.class);
        return query.getResultList();
    }

    @Override
    public void setReizigerDAO(ReizigerDAO rDao) {

    }

    @Override
    public void setProductDAO(ProductDAO productDAO) {

    }



}
