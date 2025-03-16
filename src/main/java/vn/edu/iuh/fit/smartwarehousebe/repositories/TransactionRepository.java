package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;

import java.time.LocalDateTime;

public interface TransactionRepository extends CrudRepository<Transaction, Long>,
    PagingAndSortingRepository<Transaction, Long>,
    JpaSpecificationExecutor<Transaction> {
  Page<Transaction> findTransactionsByTransactionDateBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
}