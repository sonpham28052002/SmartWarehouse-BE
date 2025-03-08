package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.unit;

import lombok.*;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GetUnitRequest implements Serializable {
    private String code;
    private String name;
}

