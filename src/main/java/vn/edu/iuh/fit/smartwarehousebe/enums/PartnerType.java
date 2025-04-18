package vn.edu.iuh.fit.smartwarehousebe.enums;

import lombok.Getter;

@Getter
public enum PartnerType {
  SUPPLIER(0),
  AGENT(1);
  private final int status;

  PartnerType(int status) {
    this.status = status;
  }
}
