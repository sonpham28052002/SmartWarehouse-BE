package vn.edu.iuh.fit.cineticketmanagebe.servies;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoftDeleteService<T> {

    @Autowired
    private EntityManager entityManager;

    /**
     * get all T include T deleted
     * @param T
     * @return List<T>
     */
    public List<T> findAllIncludeDeleted(@NonNull Class<T> T) {
        String tableName = T.getAnnotation(Table.class).name();
        String sql = "select * from " + tableName;
        Query query = entityManager.createQuery(sql);
        return query.getResultList();
    }

    /**
     * get T by id include T deleted
     * @param T
     * @param id
     * @return T
     */
    public T findByIdIncludeDeleted(@NonNull Class<T> T, @NonNull Long id) {
        String tableName = T.getAnnotation(Table.class).name();
        String sql = "SELECT * FROM " + tableName + " WHERE id = :id";
        Query query = entityManager.createNativeQuery(sql, T);
        query.setParameter("id", id);
        return (T) query.getSingleResult();
    }
}
