package vn.edu.iuh.fit.smartwarehousebe.specifications;

import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;

public class DamagedProductSpecification {
  DamagedProductSpecification() {
  }

  public static Specification<DamagedProduct> hasProductCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("inventory").get("product").get("code"), c))
        .orElse(null);
  }

  public static Specification<DamagedProduct> hasSupplierCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("inventory").get("product").get("partner").get("code"), c))
        .orElse(null);
  }

  public static Specification<DamagedProduct> hasSupplierName(String name) {
    return Optional.ofNullable(name)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("inventory").get("product").get("partner").get("name")),
                "%" + c.toLowerCase() + "%"
            )
        )
        .orElse(null);
  }

  public static Specification<DamagedProduct> hasProductName(String name) {
    return Optional.ofNullable(name)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("inventory").get("product").get("name")),
                "%" + c.toLowerCase() + "%"
            )
        )
        .orElse(null);
  }

  public static Specification<DamagedProduct> hasInventoryName(String name) {
    return Optional.ofNullable(name)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("inventory").get("storageLocation").get("name")),
                "%" + c.toLowerCase() + "%"
            )
        )
        .orElse(null);
  }


  public static Specification<DamagedProduct> hasStockTakeCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("stockTake").get("code"), c))
        .orElse(null);
  }

  public static Specification<DamagedProduct> hasTransactionCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("transaction").get("code"), c))
        .orElse(null);
  }

  public static Specification<DamagedProduct> hasStatus(String status) {
    return Optional.ofNullable(status)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("status"), c))
        .orElse(null);
  }

  public static Specification<DamagedProduct> hasExchangeType(String type) {
    return Optional.ofNullable(type)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("exchange").get("type"), c))
        .orElse(null);
  }

  public static Specification<DamagedProduct> hasExchangeStatus(String status) {
    return Optional.ofNullable(status)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) -> {
          if ("NOT_RETURNED".equals(c)) {
            return criteriaBuilder.isNull(root.get("exchange"));
          } else if ("RETURNING".equals(c)) {
            return criteriaBuilder.and(
                criteriaBuilder.isNotNull(root.get("exchange")),
                criteriaBuilder.isNull(root.get("exchange").get("type"))
            );
          } else if ("RETURNED".equals(c)) {
            return criteriaBuilder.and(
                criteriaBuilder.isNotNull(root.get("exchange")),
                criteriaBuilder.isNotNull(root.get("exchange").get("type"))
            );
          }
          return null;
        })
        .orElse(null);
  }


}
