package vn.edu.iuh.fit.smartwarehousebe.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.StorageLocation;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

public class StorageLocationSpecification {

  public static Specification<StorageLocation> hasName(String name) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);
  }

  public static Specification<StorageLocation> hasShelf(Long shelfId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
        root.get("warehouseShelf").get("id"), shelfId);
  }
}
