package DAOPsql;

import DAO.AdresDAO;
import DAO.ReizigerDAO;
import domain.Adres;
import domain.Reiziger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class AdresDAOHibernate implements AdresDAO {

    private final Session session;
    private ReizigerDAOHibernate reizigerDAO;

    public AdresDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public void save(Adres adres) {
        Transaction transaction = session.getTransaction();
        try {
            if (!transaction.isActive()) {
                transaction = session.beginTransaction();
            }
            session.persist(adres);
            if (transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void update(Adres adres) {
        Transaction tx = null;
        try {
            if (session.getTransaction().isActive()) {
                System.out.println("Transaction is already active.");
            } else {
                System.out.println("Transaction is not active. Starting a new transaction.");
                tx = session.beginTransaction();
            }
            session.merge(adres);
            if (tx != null) {
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Adres adres) {
        Transaction tx = null;
        try {
            if (session.getTransaction().isActive()) {
                System.out.println("Transaction is already active.");
            } else {
                System.out.println("Transaction is not active. Starting a new transaction.");
                tx = session.beginTransaction();
            }
            session.remove(adres);
            if (tx != null) {
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        String query = "FROM Adres WHERE id = :reizigerId";
        Query<Adres> q = session.createQuery(query, Adres.class);
        q.setParameter("reizigerId", reiziger.getId());
        return q.getSingleResult();
    }

    @Override
    public Adres findById(int adresId) {
        return session.get(Adres.class, adresId);
    }

    @Override
    public List<Adres> findAll() {
        String query = "FROM Adres";
        Query<Adres> q = session.createQuery(query, Adres.class);
        return q.getResultList();
    }

    @Override
    public void setReizigerDAO(ReizigerDAO rDao) {

    }
}