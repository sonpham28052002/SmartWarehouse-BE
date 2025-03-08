package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.storage_location;

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
public class StorageLocationResponse implements Serializable {
    private Long id;
    private String address;
    private String code;
    private String name;
    @JsonIgnoreProperties({"authorities", "warehouseManager", "warehouse"})
    private User manager;
    @JsonIgnoreProperties({"authorities", "warehouseManager", "warehouse"})
    private Set<User> staffs;
    private boolean deleted;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Long columnNum;
    private Long rowNum;
    private Long shelfNum;
}