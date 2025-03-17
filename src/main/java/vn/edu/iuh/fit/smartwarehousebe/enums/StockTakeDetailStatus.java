package vn.edu.iuh.fit.smartwarehousebe.enums;

import lombok.Getter;

@Getter
public enum StockTakeDetailStatus {
  MATCHED(0),        // Hàng hóa khớp với hệ thống (đủ hàng)
  SHORTAGE(1),       // Hàng hóa bị thiếu
  EXCESS(2),         // Hàng hóa dư thừa so với hệ thống
  DAMAGED(3),        // Hàng hóa bị hư hại
  UNVERIFIED(4),      // Chưa xác nhận kiểm kê
  CANCELLED(5);
  private final int status;

  StockTakeDetailStatus(int status) {
    this.status = status;
  }
}

