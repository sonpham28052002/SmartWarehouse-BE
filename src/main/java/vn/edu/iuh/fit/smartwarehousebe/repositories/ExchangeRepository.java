package vn.edu.iuh.fit.smartwarehousebe.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.enums.ExchangeType;
import vn.edu.iuh.fit.smartwarehousebe.models.Exchange;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long>,
    JpaSpecificationExecutor<Exchange> {

  @Query("SELECT COUNT(exch) + 1 FROM Exchange exch WHERE exch.createdDate >= :todayStart AND exch.createdDate <= :todayEnd")
  int findTodaySequence(@Param("todayStart") LocalDateTime start, @Param("todayEnd") LocalDateTime end);

  List<Exchange> findAllByCreatedDateBetweenAndTypeIn(LocalDateTime from, LocalDateTime to, List<ExchangeType> types);

  List<Exchange> findByStockTakeIdAndDeleted(Long stockTakeId, boolean deleted);
  List<Exchange> findByOriginalTransactionIdAndDeleted(Long transactionId, boolean deleted);
}