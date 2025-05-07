package vn.edu.iuh.fit.smartwarehousebe.repositories;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.Exchange;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long>,
    JpaSpecificationExecutor<Exchange> {

  @Query("SELECT COUNT(exch) + 1 FROM Exchange exch WHERE exch.createdDate >= :todayStart AND exch.createdDate <= :todayEnd")
  int findTodaySequence(@Param("todayStart") LocalDateTime start, @Param("todayEnd") LocalDateTime end);

}