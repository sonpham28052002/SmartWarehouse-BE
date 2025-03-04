package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GetWarehouseQuest {

    private String name;
    private String code;
    private String location;
    private Long managerId;
    private boolean deleted;
}
