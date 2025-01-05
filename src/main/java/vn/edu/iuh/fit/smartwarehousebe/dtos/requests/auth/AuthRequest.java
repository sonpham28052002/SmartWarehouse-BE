package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {

    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    private String lastName;
    private String firstName;

    @NotNull(message = "password cannot be null")
    private String password;
    private String token;
    private String refreshToken;
}
