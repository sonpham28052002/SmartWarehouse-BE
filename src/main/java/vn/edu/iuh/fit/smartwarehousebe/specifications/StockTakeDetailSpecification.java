package vn.edu.iuh.fit.smartwarehousebe.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTakeDetail;

public class StockTakeDetailSpecification {

  public static Specification<StockTakeDetail> hasCode(String code) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
        root.get("inventory").get("product").get("code"), code);
  }

  public static Specification<StockTakeDetail> hasStockTakeId(Long id) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
        root.get("stockTake").get("id"), id);
  }

}
