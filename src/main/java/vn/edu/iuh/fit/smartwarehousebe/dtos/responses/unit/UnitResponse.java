package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnitResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  Long id;
  String code;
  String name;
  boolean deleted;
  LocalDateTime createdDate;
  LocalDateTime lastModifiedDate;
}
