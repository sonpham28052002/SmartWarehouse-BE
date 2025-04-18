package vn.edu.iuh.fit.smartwarehousebe.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
  WAITING(0), IN_PROCESS(1), PENDING_APPROVAL(2), COMPLETE(3);
  private final int status;

  TransactionStatus(int status) {
    this.status = status;
  }
}
