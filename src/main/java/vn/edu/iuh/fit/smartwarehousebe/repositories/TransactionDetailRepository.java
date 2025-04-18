package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long>,
    JpaSpecificationExecutor
        <TransactionDetail> {

}