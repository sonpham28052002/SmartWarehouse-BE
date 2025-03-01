package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.enums.Role;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserRequest {
    private Long id;

    @NonNull
    private String code;
    @NonNull
    private String userName;
    @NonNull
    private String email;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String fullName;
    @NonNull
    private String address;
    @NonNull
    private boolean sex;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfBirth;

    private String profilePicture;

    private UserStatus status;
    @NonNull
    private Role role;
}
