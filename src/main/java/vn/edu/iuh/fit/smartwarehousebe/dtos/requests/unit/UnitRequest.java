package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.unit;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UnitRequest {
    private Long id;
    private String code;
    private String name;
}
