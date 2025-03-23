package vn.edu.iuh.fit.smartwarehousebe.enums;

import lombok.Getter;

@Getter
public enum StockTakeStatus {
  PENDING(0), IN_PROGRESS(1), COMPLETED(2), VERIFIED(3), CANCELLED(4);
  private final int status;

  StockTakeStatus(int status) {
    this.status = status;
  }
}
