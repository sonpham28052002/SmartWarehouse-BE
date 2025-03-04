package vn.edu.iuh.fit.smartwarehousebe.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.Supplier;

import java.util.Optional;

/**
 * @description
 * @author: vie
 * @date: 3/3/25
 */
public class SupplierSpecification {
    SupplierSpecification() {
    }

    public static Specification<Supplier> hasCode(String code) {
        return Optional.ofNullable(code)
                .map(c -> (Specification<Supplier>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("code"), c))
                .orElse(null);
    }

    public static Specification<Supplier> hasName(String name) {
        return Optional.ofNullable(name)
                .map(n -> (Specification<Supplier>) (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + n.toLowerCase() + "%"))
                .orElse(null);
    }

    public static Specification<Supplier> hasLocation(String location) {
        return Optional.ofNullable(location)
                .map(l -> (Specification<Supplier>) (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + l.toLowerCase() + "%"))
                .orElse(null);
    }

    public static Specification<Supplier> hasPhone(String phone) {
        return Optional.ofNullable(phone)
                .map(p -> (Specification<Supplier>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("phone"), p))
                .orElse(null);
    }

    public static Specification<Supplier> hasEmail(String email) {
        return Optional.ofNullable(email)
                .map(e -> (Specification<Supplier>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), e))
                .orElse(null);
    }

    public static Specification<Supplier> hasActive(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), isActive != null && !isActive);
    }
}
