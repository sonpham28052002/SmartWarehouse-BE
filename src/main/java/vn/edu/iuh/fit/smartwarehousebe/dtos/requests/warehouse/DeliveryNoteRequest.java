package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description
 * @author: vie
 * @date: 12/3/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNoteRequest {
    @NotNull(message = "From warehouse ID cannot be null")
    private Long fromWarehouseId;
    @NotNull(message = "To warehouse ID cannot be null")
    private Long toWarehouseId;
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    @NotNull(message = "Products cannot be null")
    private List<Item> products;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        @NotNull(message = "Product ID cannot be null")
        private Long productId;
        @NotNull(message = "Product code cannot be null")
        private String unit;
        @NotNull(message = "Product code cannot be null")
        private Integer quantity;
    }
}
