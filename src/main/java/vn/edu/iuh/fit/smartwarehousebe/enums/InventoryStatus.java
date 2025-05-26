package vn.edu.iuh.fit.smartwarehousebe.enums;

import lombok.Getter;

@Getter
public enum InventoryStatus {
  ACTIVE(0),
  INACTIVE(1);
  private final int status;

  InventoryStatus(int status) {
    this.status = status;
  }

}
