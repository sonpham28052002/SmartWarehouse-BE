package vn.edu.iuh.fit.smartwarehousebe.servies;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService<T> {

    @Autowired
    private EntityManager entityManager;

    public boolean checkCodeIsExist(@NonNull Class<T> T, String code) {
        String tableName = T.getAnnotation(Table.class).name();

        String sql = "SELECT * FROM " + tableName + " WHERE code = :code";

        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("code", code);

        return !query.getResultList().isEmpty();
    }

}
