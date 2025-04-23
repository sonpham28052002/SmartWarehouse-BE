package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;
@Repository
public interface DamagedProductRepository extends JpaRepository<DamagedProduct, Long>,
    JpaSpecificationExecutor<DamagedProduct> {

}