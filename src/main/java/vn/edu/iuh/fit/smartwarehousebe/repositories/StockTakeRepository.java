package vn.edu.iuh.fit.smartwarehousebe.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTake;

@Repository
public interface StockTakeRepository extends JpaRepository<StockTake, Long>,
    JpaSpecificationExecutor<StockTake> {

  @EntityGraph(attributePaths = {"stockTakeDetails", "stockTakeDetails.inventory"})
  Page<StockTake> findAll(Specification<StockTake> specification, Pageable pageable);

  @EntityGraph(attributePaths = {"stockTakeDetails", "stockTakeDetails.inventory"})
  Optional<StockTake> findById(Long id);
}