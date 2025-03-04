package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Value;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

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
   LocalDateTime createdDate;
   LocalDateTime lastModifiedDate;
   boolean deleted;
}