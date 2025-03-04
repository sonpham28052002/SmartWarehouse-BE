package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.supplier;

import lombok.Value;

import java.io.Serializable;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@Value
public class GetSupplierQuest implements Serializable {
    String code;
    String name;
    String location;
    String phone;
    String email;
    Boolean active;
}
