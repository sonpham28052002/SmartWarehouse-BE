package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.edu.iuh.fit.smartwarehousebe.Ids.TransactionDetailId;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, TransactionDetailId>,
    JpaSpecificationExecutor
        <TransactionDetail> {

    List<TransactionDetail> findByInventoryIdAndTransactionTypeInAndLastModifiedDateBetween(
            Long inventoryId,
            List<TransactionType> types,
            LocalDateTime from,
            LocalDateTime to);

}