package vn.edu.iuh.fit.smartwarehousebe.repositories;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {
    @EntityGraph(attributePaths = {"staffs", "manager"})
    default Page<Warehouse> findWareHouseAll(boolean excluded, @NonNull Specification<Warehouse> specification, Pageable pageable ) {
        Specification<Warehouse> finalSpecification = specification;

        if (excluded) {
            finalSpecification = finalSpecification.and((root, query, cb) -> cb.equal(root.get("deleted"), false));
        }

        return findAll(finalSpecification, pageable);
    }
}