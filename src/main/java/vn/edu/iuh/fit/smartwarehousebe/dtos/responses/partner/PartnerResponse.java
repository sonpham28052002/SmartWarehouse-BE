package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import vn.edu.iuh.fit.smartwarehousebe.enums.PartnerType;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Partner}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartnerResponse implements Serializable {

  Long id;
  String code;
  String name;
  String phone;
  String email;
  String address;
  LocalDateTime createdDate;
  LocalDateTime lastModifiedDate;
  boolean deleted;
  PartnerType type;

}