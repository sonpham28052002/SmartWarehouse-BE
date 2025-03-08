package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.Supplier;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;
@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>, JpaSpecificationExecutor<Unit> {
}