package vn.edu.iuh.fit.smartwarehousebe.specifications;

import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.Exchange;

public class ExchangeSpecification {

  ExchangeSpecification() {
  }

  public static Specification<Exchange> hasCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<Exchange>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("code"), c))
        .orElse(null);
  }

  public static Specification<Exchange> hasType(String type) {
    return Optional.ofNullable(type)
        .map(c -> (Specification<Exchange>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("type"), c))
        .orElse(null);
  }

  public static Specification<Exchange> hasTransactionCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<Exchange>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("transaction").get("code"), c))
        .orElse(null);
  }

  public static Specification<Exchange> hasSupplierName(String name) {
    return Optional.ofNullable(name)
        .map(c -> (Specification<Exchange>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("supplier").get("name"), c))
        .orElse(null);
  }

  public static Specification<Exchange> hasSupplierCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<Exchange>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("supplier").get("code"), c))
        .orElse(null);
  }

}
