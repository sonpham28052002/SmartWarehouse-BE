package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import vn.edu.iuh.fit.smartwarehousebe.enums.PartnerType;

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
  @NotNull(message = "type must not be blank")
  PartnerType type;

}