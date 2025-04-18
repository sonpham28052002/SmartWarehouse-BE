package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse;

import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user.UserResponse;

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
public class WarehouseResponse implements Serializable {
  private Long id;
  private String address;
  private String code;
  private String name;
  private UserResponse manager;
  private Set<UserResponse> staffs;
  private boolean deleted;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;
}