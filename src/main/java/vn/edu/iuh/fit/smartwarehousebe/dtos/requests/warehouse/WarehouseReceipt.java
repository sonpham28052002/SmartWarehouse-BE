package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description
 * @author: vie
 * @date: 16/3/25
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseReceipt {
  private String code;
  private LocalDateTime createdDate;
  private String supplierCode;
  private String supplierName;
  private String supplierAddress;
  private String supplierPhone;
  private String warehouseName;
  private String warehouseAddress;
  private String warehouseCode;
  private String createdBy;
  private List<WarehouseReceiptItem> items;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class WarehouseReceiptItem {
    private String productCode;
    private String sku;
    private String productName;
    private String unit;
    private Integer quantity;
  }
}