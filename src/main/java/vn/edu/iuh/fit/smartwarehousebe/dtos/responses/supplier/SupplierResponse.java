package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Supplier}
 */
@Value
public class SupplierResponse implements Serializable {
   Long id;
   String code;
   String name;
   String phone;
   String email;
   String address;
}