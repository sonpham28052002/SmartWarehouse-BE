package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.enums.Role;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse implements Serializable {

  private Long id;

  private String code;

  private String userName;

  private String email;

  private String phoneNumber;

  private String fullName;

  private String address;

  private boolean sex;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime dateOfBirth;

  private String profilePicture;

  private UserStatus status;

  private Role role;

  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;
  private boolean deleted;

}
