package vn.edu.iuh.fit.smartwarehousebe.specifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;

import java.util.Optional;

/**
 * @description
 * @author: vie
 * @date: 3/3/25
 */
@Slf4j
public class ProductSpecification {

  ProductSpecification() {
  }

  public static Specification<Product> hasCode(String code) {
    return Optional.ofNullable(code)
        .map(c -> (Specification<Product>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("code"), c))
        .orElse(null);
  }

  public static Specification<Product> hasName(String name) {
    return Optional.ofNullable(name)
        .map(n -> (Specification<Product>) (root, query, criteriaBuilder) ->
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                "%" + n.toLowerCase() + "%"))
        .orElse(null);
  }

  public static Specification<Product> hasSku(String sku) {
    return Optional.ofNullable(sku)
        .map(s -> (Specification<Product>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("sku"), s))
        .orElse(null);
  }

  public static Specification<Product> hasPartnerId(Long partnerId) {
    return Optional.ofNullable(partnerId)
        .map(id -> (Specification<Product>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get("partner").get("id"), id))
        .orElse(null);
  }

  public static Specification<Product> hasActive(Boolean isActive) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("deleted"), isActive != null && !isActive);
  }
}
