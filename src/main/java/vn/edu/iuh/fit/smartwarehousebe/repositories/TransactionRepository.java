package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long>,
        PagingAndSortingRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {
}