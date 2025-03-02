package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse;

import java.io.Serializable;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Warehouse}
 */
public record CreateWarehouseRequest(String address, String code, String name, Integer managerId) implements Serializable {
}