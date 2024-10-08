package DAOPsql;
import DAO.ProductDAO;
import domain.OVChipkaart;
import domain.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import org.hibernate.query.Query;


public class ProductDAOHibernate implements  ProductDAO{
    private final Session session;

    public ProductDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Product product) {
        Transaction tx = null;

        try {
            if (session.getTransaction().isActive()) {
                System.out.println("Transaction is already active.");
            } else {
                System.out.println("Transaction is not active. Starting a new transaction.");
                tx = session.beginTransaction();
            }
            session.persist(product);
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
    public boolean update(Product product) {
        Transaction tx = null;
        try {
            if (session.getTransaction().isActive()) {
                System.out.println("Transaction is already active.");
            } else {
                System.out.println("Transaction is not active. Starting a new transaction.");
                tx = session.beginTransaction();
            }
            session.merge(product);
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

    public boolean delete(Product product) {
        Transaction tx = null;
        try {
            if (session.getTransaction().isActive()) {
                System.out.println("Transaction is already active.");
            } else {
                System.out.println("Transaction is not active. Starting a new transaction.");
                tx = session.beginTransaction();
            }
            session.remove(product);
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

    public List<Product> findAll() {
        Query query = session.createQuery("from Product");
        return query.list();
    }

    @Override

    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        Query query = session.createQuery("SELECT p FROM Product p JOIN p.ovChipkaarten o WHERE o.kaartNummer = :kaartNummer");
        query.setParameter("kaartNummer", ovChipkaart.getKaartNummer());
        return query.list();
    }




}
