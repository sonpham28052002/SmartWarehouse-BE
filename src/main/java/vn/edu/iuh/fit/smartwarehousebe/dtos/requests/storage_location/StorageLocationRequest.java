package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.storage_location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StorageLocationRequest implements Serializable {
    private Long columnNum;
    private Long rowNum;
    private Long shelfNum;
    private String name;
    private Long warehouseId;
    private Long productId;
}