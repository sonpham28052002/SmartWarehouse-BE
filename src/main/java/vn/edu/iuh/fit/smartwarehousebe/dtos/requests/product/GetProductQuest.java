package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetProductQuest implements Serializable {

  String code;
  String name;
  String sku;
  Long partnerId;
  Boolean active;
}
