package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

  private String token;
  private String refreshToken;
  private Long exp;
  @JsonIgnoreProperties({"authorities", "accountNonExpired", "credentialsNonExpired",
      "accountNonLocked", "warehouseManager", "warehouse"})
  private User user;
  private String message;
  private int code;
}
