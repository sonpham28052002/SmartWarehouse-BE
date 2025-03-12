package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product;

import com.zaxxer.hikari.util.FastList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import vn.edu.iuh.fit.smartwarehousebe.models.ConversionUnit;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Product}
 */
@Value
public class CreateProductRequest implements Serializable {

  @NotBlank(message = "Code is required")
  String code;
  String sku;
  @NotBlank(message = "Name is required")
  String name;
  String description;
  String image;
  @NotNull(message = "Supplier id is required")
  Long supplierId;
  Double unitWeight;
  Long unitId;
  List<ConversionUnit> conversionUnits;
}