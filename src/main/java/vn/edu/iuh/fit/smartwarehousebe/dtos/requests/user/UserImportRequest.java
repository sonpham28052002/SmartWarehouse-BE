package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.enums.Role;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;

import java.time.LocalDateTime;

@Value
public class UserImportRequest {
    @Pattern(regexp = "USER-\\d{5}", message = "Code must be in the format USER-####")  String code;
    @NotBlank String userName;
    @NotBlank String password;
    @NotBlank String email;
    @NotBlank String phoneNumber;
    @NotBlank String fullName;
    @NotBlank String address;
    boolean sex;
    @NonNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateOfBirth;
    String profilePicture;
    UserStatus status;
    @NonNull Role role;
}
