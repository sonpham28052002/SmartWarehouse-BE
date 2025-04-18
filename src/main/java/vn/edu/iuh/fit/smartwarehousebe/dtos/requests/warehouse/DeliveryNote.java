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
public class DeliveryNote {
  private String code;
  private LocalDateTime createdDate;
  private String fromWarehouseName;
  private String fromWarehouseAddress;
  private String fromWarehouseCode;
  private String toWarehouseName;
  private String toWarehouseAddress;
  private String toWarehouseCode;
  private String createdBy;
  private List<DeliveryNoteItem> items;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DeliveryNoteItem {
    private String productCode;
    private String sku;
    private String productName;
    private String unit;
    private Integer quantity;
  }
}