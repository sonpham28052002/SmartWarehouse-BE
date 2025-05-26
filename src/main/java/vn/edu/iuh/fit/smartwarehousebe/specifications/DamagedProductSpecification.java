package vn.edu.iuh.fit.smartwarehousebe.specifications;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;
import vn.edu.iuh.fit.smartwarehousebe.models.Partner;

public class DamagedProductSpecification {
  DamagedProductSpecification() {
  }

  public static Specification<DamagedProduct> hasDeleted(boolean isDeleted) {
    return Optional.ofNullable(isDeleted)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("deleted"), c))
        .orElse(null);
  }

  public static Specification<DamagedProduct> hasProductCode(String code) {
    return equalsSpec(code, "stockTakeDetail", "inventory", "product", "code");
  }

  public static Specification<DamagedProduct> hasSupplierCode(String code) {
    return equalsSpec(code, "stockTakeDetail", "inventory", "product", "partner", "code");
  }

  public static Specification<DamagedProduct> hasSupplierName(String name) {
    return likeSpec(name, "stockTakeDetail", "inventory", "product", "partner", "name");
  }

  public static Specification<DamagedProduct> hasProductName(String name) {
    return likeSpec(name, "stockTakeDetail", "inventory", "product", "name");
  }

  public static Specification<DamagedProduct> hasInventoryName(String name) {
    return likeSpec(name, "stockTakeDetail", "inventory", "storageLocation", "name");
  }

  public static Specification<DamagedProduct> hasTransactionProductCode(String code) {
    return equalsSpec(code, "transactionDetail", "inventory", "product", "code");
  }

  public static Specification<DamagedProduct> hasTransactionSupplierCode(String code) {
    return equalsSpec(code, "transactionDetail", "inventory", "product", "partner", "code");
  }

  public static Specification<DamagedProduct> hasTransactionSupplierName(String name) {
    return likeSpec(name, "transactionDetail", "inventory", "product", "partner", "name");
  }

  public static Specification<DamagedProduct> hasTransactionProductName(String name) {
    return likeSpec(name, "transactionDetail", "inventory", "product", "name");
  }

  public static Specification<DamagedProduct> hasTransactionInventoryName(String name) {
    return likeSpec(name, "transactionDetail", "inventory", "storageLocation", "name");
  }


  private static Specification<DamagedProduct> equalsSpec(String value, String... path) {
    return (root, query, cb) -> {
      if (value == null) {
        return cb.conjunction();
      }

      Path<String> pathToAttribute = getPath(root, path);

      if (pathToAttribute == null) {
        return cb.conjunction();
      }

      return cb.equal(pathToAttribute, value);
    };
  }


  private static Specification<DamagedProduct> likeSpec(String value, String... path) {
    return (root, query, cb) -> {
      if (value == null) {
        return cb.conjunction();
      }

      Path<String> pathToAttribute = getPath(root, path);

      if (pathToAttribute == null) {
        return cb.conjunction();
      }

      return cb.like(cb.lower(pathToAttribute), "%" + value.toLowerCase() + "%");
    };
  }

  private static Path<String> getPath(Root<DamagedProduct> root, String... attributes) {
    Path<?> path = root;
    for (String attr : attributes) {
      path = path.get(attr);
    }
    return (Path<String>) path;
  }


  public static Specification<DamagedProduct> hasStockTakeCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("stockTakeDetail").get("stockTake").get("code"), c))
        .orElse(null);
  }

  public static Specification<DamagedProduct> hasTransactionCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("transactionDetail").get("transaction").get("code"), c))
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
            root.get("exchangeType"), c))
        .orElse(null);
  }

  public static Specification<DamagedProduct> hasExchangeStatus(String status) {
    return Optional.ofNullable(status)
        .map(c -> (Specification<DamagedProduct>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("status"), c))
        .orElse(null);
  }
}
