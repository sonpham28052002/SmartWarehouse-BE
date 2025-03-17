package vn.edu.iuh.fit.smartwarehousebe.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTake;

public class StockTakeSpecification {

  public static Specification<StockTake> hasCode(String code) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
        root.get("warehouse").get("code"), code);
  }

  public static Specification<StockTake> hasStatus(int status) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
  }
}
