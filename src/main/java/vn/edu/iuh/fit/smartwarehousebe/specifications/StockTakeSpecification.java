package vn.edu.iuh.fit.smartwarehousebe.specifications;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTake;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;

public class StockTakeSpecification {

  public static Specification<StockTake> hasWarehouseCode(String code) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("warehouse").get("code"), code);
  }

  public static Specification<StockTake> hasStatus(int status) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
  }

  public static Specification<StockTake> hasCode(String code) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("code"), code);
  }

  public static Specification<StockTake> hasStockTakeDateBetween(LocalDateTime startDate,
      LocalDateTime endDate) {
    return (root, query, criteriaBuilder) -> {
      if (startDate != null && endDate != null) {
        return criteriaBuilder.between(root.get("createdDate"), startDate, endDate);
      }
      return Optional.ofNullable(startDate)
          .map(date -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), date))
          .orElseGet(() -> Optional.ofNullable(endDate)
              .map(date -> criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), date))
              .orElse(criteriaBuilder.conjunction()));
    };
  }
}
