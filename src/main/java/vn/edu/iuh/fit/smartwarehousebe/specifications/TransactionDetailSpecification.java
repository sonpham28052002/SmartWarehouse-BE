package vn.edu.iuh.fit.smartwarehousebe.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTakeDetail;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;

public class TransactionDetailSpecification {

  public static Specification<TransactionDetail> hasTransactionId(Long transactionId) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("transaction").get("id"), transactionId);
  }

  public static Specification<TransactionDetail> hasCode(String code) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
        root.get("product").get("code"), code);
  }

  public static Specification<TransactionDetail> hasName(String name) {
    return (root, query, criteriaBuilder) -> {
      if (name == null || name.trim().isEmpty()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.like(root.get("product").get("name"),
          "%" + name + "%");
    };
  }

}
