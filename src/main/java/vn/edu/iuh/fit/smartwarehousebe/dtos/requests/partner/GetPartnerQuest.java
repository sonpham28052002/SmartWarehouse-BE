package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner;

import lombok.Value;

import java.io.Serializable;
import vn.edu.iuh.fit.smartwarehousebe.enums.PartnerType;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@Value
public class GetPartnerQuest implements Serializable {

  String code;
  String name;
  String location;
  String phone;
  String email;
  Boolean active;
  PartnerType type;
}
