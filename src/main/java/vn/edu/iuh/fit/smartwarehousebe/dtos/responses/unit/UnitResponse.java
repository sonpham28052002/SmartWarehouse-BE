package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class UnitResponse {
    Long id;
    String code;
    String name;
    boolean deleted;
    LocalDateTime createdDate;
    LocalDateTime lastModifiedDate;
}
