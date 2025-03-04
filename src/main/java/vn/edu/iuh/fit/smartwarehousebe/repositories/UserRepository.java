package vn.edu.iuh.fit.smartwarehousebe.repositories;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findUserByUserName(String name);

    @Query("SELECT u FROM User u JOIN Warehouse w ON u.id = w.manager.id")
    List<User> findUsersWithManagerInWarehouse();

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT w.manager.id FROM Warehouse w WHERE w.manager IS NOT NULL) and u.role = 3")
    List<User> findUsersManagerNotInWarehouse();

    public Page<User> findUserByDeleted(@NonNull Class<User> entityClass, Specification<User> specification, Pageable pageable, boolean includeDeleted);

    default Page<User> findAllUsers(@NonNull Specification<User> specification, Pageable pageable, boolean includeDeleted) {
        Specification<User> finalSpecification = specification;

        // Add a condition to include or exclude deleted users
        if (!includeDeleted) {
            finalSpecification = finalSpecification.and((root, query, cb) -> cb.equal(root.get("deleted"), false));
        }

        // Use the findAll method with the modified specification
        return findAll(finalSpecification, pageable);
    }

    default List<User> getAllUser(Specification<User> specification, boolean excluded) {
        Specification<User> finalSpecification = specification;

        if (excluded) {
            finalSpecification = finalSpecification.and((root, query, cb) -> cb.equal(root.get("deleted"), false));
        }

        return findAll(finalSpecification);
    }
}