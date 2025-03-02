package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse;

import java.io.Serializable;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Warehouse}
 */
public record WarehouseResponse(Integer id, String address, String code, String name, Integer manager) implements Serializable {
}