package eui.miw.pfm.models.dao.jpa;

import eui.miw.pfm.models.dao.TransactionGenericDAO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;

public class JPATransactionGenericDAO<T, ID> implements TransactionGenericDAO<T, ID> {

    private final transient Class<T> persistentClass;

    transient protected EntityManager entityManager;

    public JPATransactionGenericDAO(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
        this.entityManager = ((JPADAOFactory) JPADAOFactory.getFactory()).getEntityManager();
    }

    @Override
    public void create(final T entity) {
        Logger.getLogger(JPATransactionGenericDAO.class).info("create: " + entity);
        if (entityManager.getTransaction().isActive()) {
            entityManager.persist(entity);
        } else {
            entityManager.getTransaction().begin();
            try {
                entityManager.persist(entity);
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                Logger.getLogger(JPATransactionGenericDAO.class).error("create: " + e);
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
            }
        }
    }

    @Override
    public T read(final ID code) {
        return entityManager.find(persistentClass, code);
    }

    @Override
    public void update(final T entity) {
        Logger.getLogger(JPATransactionGenericDAO.class).info("update: " + entity);
        if (entityManager.getTransaction().isActive()) {
            entityManager.merge(entity);
        } else {
            entityManager.getTransaction().begin();
            try {
                entityManager.merge(entity);
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                Logger.getLogger(JPATransactionGenericDAO.class).error("update: " + e);
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
            }
        }
    }

    // entity debe estar en estado de "Managed"
    @Override
    public void delete(final T entity) {
        Logger.getLogger(JPATransactionGenericDAO.class).info("delete: " + entity);
        if (entityManager.getTransaction().isActive()) {
            entityManager.remove(entity);
        } else {
            entityManager.getTransaction().begin();
            try {
                entityManager.remove(entity);
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                Logger.getLogger(JPATransactionGenericDAO.class).error("delete: " + e);
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
            }
        }
    }

    @Override
    public void deleteByID(final ID code) {
        assert code != null;
        final T entity = this.read(code);
        assert entity != null;
        this.delete(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> find(final String[] attributes, final String[] values, final String order, final int index, final int size) {
        assert attributes != null;
        assert values != null;
        assert order != null;

// Se crea un criterio de consulta
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        assert criteriaBuilder != null;

        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.persistentClass);
        assert criteriaQuery != null;

        // Se establece la clausula FROM
        final Root<T> root = criteriaQuery.from(this.persistentClass);
        assert root != null;

        // Se establece la clausula SELECT
        criteriaQuery.select(root); // criteriaQuery.multiselect(root.get(atr))

        // Se configuran los predicados, combinados por AND
        Predicate predicate = criteriaBuilder.conjunction();
        assert predicate != null;
        for (int i = 0; i < attributes.length; i++) {
            final Predicate sig = criteriaBuilder.like(root.get(attributes[i]).as(String.class),
                    values[i]);
            assert sig != null;
            predicate = criteriaBuilder.and(predicate, sig);
        }
        // Se establece el WHERE
        criteriaQuery.where(predicate);

        // Se establece el orden
        if (order != null && !order.isEmpty()) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(order)));
        }

        // Se crea el resultado
        if (index >= 0 && size > 0) {
            final TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
            typedQuery.setFirstResult(index);
            typedQuery.setMaxResults(size); // Se realiza la query
            return typedQuery.getResultList();
        } else {
            // Se realiza la query
            final Query query = entityManager.createQuery(criteriaQuery);
            assert query != null;
            return query.getResultList();
        }
    }

    @Override
    public List<T> find(final String[] attributes, final String[] values) {
        assert attributes != null;
        assert values != null;
        return this.find(attributes, values, null, -1, -1);
    }

    @Override
    public List<T> findAll() {
        // Se crea un criterio de consulta
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        assert criteriaBuilder != null;

        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.persistentClass);
        assert criteriaQuery != null;

        // Se establece la clausula FROM
        final Root<T> root = criteriaQuery.from(this.persistentClass);
        assert root != null;

        // Se establece la clausula SELECT
        criteriaQuery.select(root); // criteriaQuery.multiselect(root.get(atr))

        // No existen predicados
        // Se realiza la query
        final TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        assert typedQuery != null;
        typedQuery.setFirstResult(0); // El primero es 0
        typedQuery.setMaxResults(0); // Se realiza la query, se buscan todos
        return typedQuery.getResultList();

        // Equivalente a return this.find(new String[0], new String[0], null, 0,
        // 0);
    }

    public void close() {
        this.entityManager.close();
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    @Override
    public void begin() {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
    }

    @Override
    public void commit() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void rollback() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

}
