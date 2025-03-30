package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Partner}
 */
@Value
public class CreatePartnerRequest implements Serializable {

  @NotBlank(message = "Code must not be blank")
  String code;
  @NotBlank(message = "Name must not be blank")
  String name;
  @NotBlank(message = "Phone must not be blank")
  String phone;
  @NotBlank(message = "Email must not be blank")
  String email;
  @NotBlank(message = "Address must not be blank")
  String address;
}