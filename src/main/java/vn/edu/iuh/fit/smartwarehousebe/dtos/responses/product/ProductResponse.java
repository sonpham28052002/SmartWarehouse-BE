package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Product}
 */
@Value
public class ProductResponse implements Serializable {
   Long id;
   String code;
   String sku;
   String name;
   String description;
   String image;
   Long supplierId;
}