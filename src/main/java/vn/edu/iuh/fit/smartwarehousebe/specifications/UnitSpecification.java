package vn.edu.iuh.fit.smartwarehousebe.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.Supplier;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;

import java.util.Optional;

public class UnitSpecification {
    UnitSpecification() {
    }

    public static Specification<Unit> hasCode(String code) {
        return Optional.ofNullable(code)
                .map(c -> (Specification<Unit>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("code"), c))
                .orElse(null);
    }

    public static Specification<Unit> hasName(String name) {
        return Optional.ofNullable(name)
                .map(n -> (Specification<Unit>) (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + n.toLowerCase() + "%"))
                .orElse(null);
    }

    public static Specification<Unit> hasActive(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), isActive != null && !isActive);
    }
}
