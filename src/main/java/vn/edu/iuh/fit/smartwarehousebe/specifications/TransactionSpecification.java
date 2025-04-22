package vn.edu.iuh.fit.smartwarehousebe.specifications;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionStatus;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;

import java.util.Optional;

/**
 * @description
 * @author: vie
 * @date: 3/3/25
 */
@Slf4j
public class TransactionSpecification {

  TransactionSpecification() {
  }

  public static Specification<Transaction> hasTransactionType(String transactionType) {
    return Optional.ofNullable(transactionType)
        .map(
            c -> (Specification<Transaction>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("transactionType"), c))
        .orElse(null);
  }

  public static Specification<Transaction> hasTransactionDate(String transactionDate) {
    return Optional.ofNullable(transactionDate)
        .map(
            c -> (Specification<Transaction>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("transactionDate"), c))
        .orElse(null);
  }

  public static Specification<Transaction> hasTransactionExecutor(Long executor) {
    return Optional.ofNullable(executor)
        .map(
            c -> (Specification<Transaction>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("executor").get("id"), c))
        .orElse(null);
  }

  public static Specification<Transaction> hasTransactionWarehouse(Long warehouse) {
    return Optional.ofNullable(warehouse)
        .map(
            c -> (Specification<Transaction>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("warehouse").get("id"), c))
        .orElse(null);
  }

  public static Specification<Transaction> hasTransactionTransfer(Long transfer) {
    return Optional.ofNullable(transfer)
        .map(
            c -> (Specification<Transaction>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("transfer").get("id"), c))
        .orElse(null);
  }

  public static Specification<Transaction> hasStatus(TransactionStatus status) {
    return Optional.ofNullable(status)
        .map(
            c -> (Specification<Transaction>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("status"), c))
        .orElse(null);
  }

  public static Specification<Transaction> hasTransactionPartner(Long partner) {
    return Optional.ofNullable(partner)
        .map(
            c -> (Specification<Transaction>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("partner").get("id"), c))
        .orElse(null);
  }

  public static Specification<Transaction> hasCode(String code) {
    return Optional.ofNullable(code)
            .map(c -> (Specification<Transaction>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                            root.get("code"), c))
            .orElse(null);
  }

  public static Specification<Transaction> hasTransactionDateBetween(LocalDateTime startDate,
      LocalDateTime endDate) {
    return (root, query, criteriaBuilder) -> {
      if (startDate != null && endDate != null) {
        return criteriaBuilder.between(root.get("transactionDate"), startDate, endDate);
      }
      return Optional.ofNullable(startDate)
          .map(date -> criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), date))
          .orElseGet(() -> Optional.ofNullable(endDate)
              .map(date -> criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), date))
              .orElse(criteriaBuilder.conjunction()));
    };
  }
}
