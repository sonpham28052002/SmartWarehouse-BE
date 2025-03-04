package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

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
}