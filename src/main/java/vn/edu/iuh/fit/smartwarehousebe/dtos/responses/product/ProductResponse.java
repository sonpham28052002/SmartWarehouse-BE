package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.ConversionUnit.ConversionUnitResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Product}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductResponse implements Serializable {

  Long id;
  String code;
  String sku;
  String name;
  String description;
  String image;
  Long supplierId;
  LocalDateTime createdDate;
  LocalDateTime lastModifiedDate;
  Double unitWeight;
  boolean deleted;
  @JsonIgnoreProperties({"product"})
  Unit unit;
  @JsonIgnoreProperties({"product", "inventories"})
  List<ConversionUnitResponse> conversionUnits;
}