package vn.edu.iuh.fit.smartwarehousebe.repositories;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;

@Repository
public interface DamagedProductRepository extends JpaRepository<DamagedProduct, Long>,
    JpaSpecificationExecutor<DamagedProduct> {

//  Set<DamagedProduct> findByStockTakeId(Long stockTakeId);
//
//  Set<DamagedProduct> findByTransactionId(Long transactionId);

}