package vn.edu.iuh.fit.smartwarehousebe.specifications;

import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;

public class InventorySpecification {

  InventorySpecification(){}

  public static Specification<Inventory> hasProductCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<Inventory>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("product").get("code"), c))
        .orElse(null);
  }

  public static Specification<Inventory> hasWarehouseCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<Inventory>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("storageLocation").get("warehouseShelf").get("warehouse").get("code"), c))
        .orElse(null);
  }

  public static Specification<Inventory> hasProductName(String name) {
    return Optional.ofNullable(name)
        .map(c -> (Specification<Inventory>) (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("product").get("name")),
                "%" + c.toLowerCase() + "%"
            )
        )
        .orElse(null);
  }

  public static Specification<Inventory> hasWarehouseName(String name) {
    return Optional.ofNullable(name)
        .map(c -> (Specification<Inventory>) (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("storageLocation").get("warehouseShelf").get("warehouse").get("name")),
                "%" + c.toLowerCase() + "%"
            )
        )
        .orElse(null);
  }
}