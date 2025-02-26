package vn.edu.iuh.fit.smartwarehousebe.repositories;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findUserByUserName(String name);

    public Page<User> findUserByDeleted(@NonNull Class<User> entityClass, Specification<User> specification, Pageable pageable, boolean includeDeleted);

    default Page<User> findUserByDeleted(@NonNull Specification<User> specification, Pageable pageable, boolean includeDeleted) {
        Specification<User> finalSpecification = specification;

        // Add a condition to include or exclude deleted users
        if (!includeDeleted) {
            finalSpecification = finalSpecification.and((root, query, cb) -> cb.equal(root.get("deleted"), false));
        }

        // Use the findAll method with the modified specification
        return findAll(finalSpecification, pageable);
    }
}