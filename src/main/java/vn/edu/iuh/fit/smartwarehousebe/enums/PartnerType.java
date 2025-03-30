package vn.edu.iuh.fit.smartwarehousebe.enums;

import lombok.Getter;

@Getter
public enum PartnerType {
  SUPPLIER(0),
  AGENT(1);
  private final int role;

  PartnerType(int role) {
    this.role = role;
  }
}
