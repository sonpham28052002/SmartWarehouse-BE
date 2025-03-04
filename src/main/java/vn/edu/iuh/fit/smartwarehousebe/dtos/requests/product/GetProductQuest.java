package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product;

import lombok.Value;

import java.io.Serializable;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@Value
public class GetProductQuest implements Serializable {
    String code;
    String name;
    String sku;
    Long supplierId;
    Boolean active;
}
