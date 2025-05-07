package vn.edu.iuh.fit.smartwarehousebe.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.enums.PartnerType;
import vn.edu.iuh.fit.smartwarehousebe.models.Partner;

import java.util.Optional;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

/**
 * @description
 * @author: vie
 * @date: 3/3/25
 */
public class PartnerSpecification {

  PartnerSpecification() {
  }

  public static Specification<Partner> hasCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<Partner>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("code"), c))
        .orElse(null);
  }

  public static Specification<Partner> hasName(String name) {
    return Optional.ofNullable(name)
        .map(n -> (Specification<Partner>) (root, query, criteriaBuilder) ->
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                "%" + n.toLowerCase() + "%"))
        .orElse(null);
  }

  public static Specification<Partner> hasLocation(String location) {
    return Optional.ofNullable(location)
        .map(l -> (Specification<Partner>) (root, query, criteriaBuilder) ->
            criteriaBuilder.like(criteriaBuilder.lower(root.get("address")),
                "%" + l.toLowerCase() + "%"))
        .orElse(null);
  }

  public static Specification<Partner> hasPhone(String phone) {
    return Optional.ofNullable(phone)
        .map(p -> (Specification<Partner>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("phone"), p))
        .orElse(null);
  }

  public static Specification<Partner> hasEmail(String email) {
    return Optional.ofNullable(email)
        .map(e -> (Specification<Partner>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("email"), e))
        .orElse(null);
  }

  public static Specification<Partner> hasType(PartnerType type) {
    return Optional.ofNullable(type)
        .map(e -> (Specification<Partner>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("type"), e))
        .orElse(null);
  }

  public static Specification<Partner> hasActive(Boolean isActive) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("deleted"), isActive != null && !isActive);
  }
}
