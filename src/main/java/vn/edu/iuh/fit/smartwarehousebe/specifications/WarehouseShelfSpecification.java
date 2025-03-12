package vn.edu.iuh.fit.smartwarehousebe.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.models.WarehouseShelf;

public class WarehouseShelfSpecification {

  public static Specification<WarehouseShelf> hasWareHouse(Long wareHouseID) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("warehouse").get("id"),
        wareHouseID);
  }

}
