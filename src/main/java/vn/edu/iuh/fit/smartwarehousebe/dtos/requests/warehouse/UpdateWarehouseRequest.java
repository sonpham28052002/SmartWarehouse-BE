package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse;

import java.io.Serializable;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Warehouse}
 */
public record UpdateWarehouseRequest(Long id, String address, String code, String name, Long managerId) implements Serializable {
}