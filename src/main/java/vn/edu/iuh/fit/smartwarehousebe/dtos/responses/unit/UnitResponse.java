package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UnitResponse {

  Long id;
  String code;
  String name;
  boolean deleted;
  LocalDateTime createdDate;
  LocalDateTime lastModifiedDate;
}
