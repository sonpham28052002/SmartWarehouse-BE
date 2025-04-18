package vn.edu.iuh.fit.smartwarehousebe.repositories;

import aj.org.objectweb.asm.commons.Remapper;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>,
    PagingAndSortingRepository<Transaction, Long>,
    JpaSpecificationExecutor<Transaction> {

  Page<Transaction> findTransactionsByTransactionDateBetween(LocalDateTime from, LocalDateTime to,
      Pageable pageable);

  Optional<Transaction> findByIdAndTransactionType(Long id, TransactionType transactionType);

  @Query(value = "SELECT DATE(t.transaction_date) AS day, COUNT(t.id) AS transaction_count " +
      "FROM transactions t " +
      "WHERE t.transaction_date BETWEEN :startDate AND :endDate " +
      "AND t.status = :status " +
      "AND t.transaction_type = :type " +
      "GROUP BY DATE(t.transaction_date) " +
      "ORDER BY day", nativeQuery = true)
  List<Object[]> countTransactionsByDateRangeAndStatus(String startDate, String endDate,
      String status, String type);
}