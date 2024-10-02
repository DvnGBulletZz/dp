package DAO;

import domain.Reiziger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {

    private EntityManager em;


    public ReizigerDAOHibernate(EntityManager em) {
        this.em = em;

    }

    @Override
    public boolean save(Reiziger reiziger) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(reiziger);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(reiziger);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(em.contains(reiziger) ? reiziger : em.merge(reiziger));
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        return em.find(Reiziger.class, id);
    }

    @Override
    public List<Reiziger> findByGbdatum(java.sql.Date date) {
        String query = "FROM Reiziger r WHERE r.geboortedatum = :gbdatum";
        TypedQuery<Reiziger> tq = em.createQuery(query, Reiziger.class);
        tq.setParameter("gbdatum", date);
        return tq.getResultList();
    }

    @Override
    public List<Reiziger> findAll() {
        String query = "FROM Reiziger";
        TypedQuery<Reiziger> tq = em.createQuery(query, Reiziger.class);
        return tq.getResultList();
    }
}