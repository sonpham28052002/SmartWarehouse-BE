package vn.edu.iuh.fit.smartwarehousebe.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.enums.Role;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

import java.util.List;


public class UserSpecification {

    public static Specification<User> hasCode(String code) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("code"), code);
    }

    public static Specification<User> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("fullName"), "%" + name + "%");
    }

    public static Specification<User> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<User> hasRoles(List<Integer> roles) {
        return (root, query, criteriaBuilder) -> root.get("role").in(roles);
    }

    public static Specification<User> hasWareHouseNotNull() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("warehouse"));
    }

    public static Specification<User> hasWareHouseIsNull() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("warehouse"));
    }

}
