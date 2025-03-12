package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import vn.edu.iuh.fit.smartwarehousebe.models.ConversionUnit;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Product}
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
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
  List<ConversionUnit> conversionUnits;
}