package vn.edu.iuh.fit.smartwarehousebe.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;


public class WarehouseSpecification {
   private WarehouseSpecification() {}

   public static Specification<Warehouse> hasCode(String code) {
      return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("code"), code);
   }

   public static Specification<Warehouse> nameLike(String name) {
      return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
   }

   public static Specification<Warehouse> addressLike(String address) {
      return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("address"), "%" + address + "%");
   }

   public static Specification<Warehouse> hasManager(Long managerId) {
      return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("manager").get("id"), managerId);
   }

   public static Specification<Warehouse> hasDeleted(boolean deleted) {
      return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), deleted);
   }
}
