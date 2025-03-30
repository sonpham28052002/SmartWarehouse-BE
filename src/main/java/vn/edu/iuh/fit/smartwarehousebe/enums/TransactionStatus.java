package vn.edu.iuh.fit.smartwarehousebe.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
  WAITING(0), IN_PROCESS(1), COMPLETE(2);
  private final int status;

  TransactionStatus(int status) {
    this.status = status;
  }
}
