package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UpdateWarehouseRequest implements Serializable{

    private String address;
    private String code;
    private String name;
    private Long managerId;
    private Set<Long> staffIDs;
}
