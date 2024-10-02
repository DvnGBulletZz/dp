package DAOPsql;

import DAO.AdresDAO;
import DAO.ReizigerDAO;
import domain.Reiziger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.sql.Date;
import java.util.List;
import org.hibernate.query.Query;
import domain.Adres;

public class ReizigerDAOHibernate implements ReizigerDAO {

    private final Session session;
    private final AdresDAO adresDAO;

    public ReizigerDAOHibernate(Session session, AdresDAO adresDAO) {
        this.session = session;
        this.adresDAO = adresDAO;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        Transaction tx = null;

        try {
            if (session.getTransaction().isActive()) {
                System.out.println("Transaction is already active.");
            } else {
                System.out.println("Transaction is not active. Starting a new transaction.");
                tx = session.beginTransaction();
            }
            session.persist(reiziger);
            if (tx != null) {
                tx.commit();
            }
            if (this.adresDAO != null && reiziger.getAdres() != null) {
                Adres adres = reiziger.getAdres();
                this.adresDAO.save(adres);
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
    public boolean update(Reiziger reiziger) {
        Transaction tx = null;
        try {
            if (session.getTransaction().isActive()) {
                System.out.println("Transaction is already active.");
            } else {
                System.out.println("Transaction is not active. Starting a new transaction.");
                tx = session.beginTransaction();
            }
            session.update(reiziger);
            if (tx != null) {
                tx.commit();
            }
            if (this.adresDAO != null && reiziger.getAdres() != null) {
                Adres adres = reiziger.getAdres();
                this.adresDAO.update(adres);
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
    public boolean delete(Reiziger reiziger) {
        Transaction tx = null;
        try {
            if (session.getTransaction().isActive()) {
                System.out.println("Transaction is already active.");
            } else {
                System.out.println("Transaction is not active. Starting a new transaction.");
                tx = session.beginTransaction();
            }
            session.remove(reiziger);
            if (this.adresDAO != null && reiziger.getAdres() != null) {
                Adres adres = reiziger.getAdres();
                this.adresDAO.delete(adres);
            }
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
    public Reiziger findById(int id) {
        return session.get(Reiziger.class, id);
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) {
        String query = "FROM Reiziger r WHERE r.geboortedatum = :geboortedatum";
        Query<Reiziger> q = session.createQuery(query, Reiziger.class);
        q.setParameter("geboortedatum", date);
        return q.getResultList();
    }

    @Override
    public List<Reiziger> findAll() {
        String query = "FROM Reiziger";
        Query<Reiziger> q = session.createQuery(query, Reiziger.class);
        return q.getResultList();
    }
}