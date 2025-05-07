package vn.edu.iuh.fit.smartwarehousebe.repositories;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTake;

@Repository
public interface StockTakeRepository extends JpaRepository<StockTake, Long>,
    JpaSpecificationExecutor<StockTake> {

  @EntityGraph(attributePaths = {"stockTakeDetails", "stockTakeDetails.inventory"})
  Page<StockTake> findAll(Specification<StockTake> specification, Pageable pageable);

  @EntityGraph(attributePaths = {"stockTakeDetails", "stockTakeDetails.inventory"})
  Optional<StockTake> findById(Long id);

  @Query("SELECT COUNT(s) + 1 FROM StockTake s WHERE s.createdDate >= :todayStart AND s.createdDate <= :todayEnd")
  int findTodaySequence(@Param("todayStart") LocalDateTime start, @Param("todayEnd") LocalDateTime end);

  Optional<StockTake> getByCode(String code);
}