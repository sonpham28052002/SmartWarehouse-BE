package vn.edu.iuh.fit.cineticketmanagebe.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.cineticketmanagebe.models.User;

public class UserSpecification {

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<User> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

}
