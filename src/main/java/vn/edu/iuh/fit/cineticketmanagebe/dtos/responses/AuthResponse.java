package vn.edu.iuh.fit.cineticketmanagebe.dtos.responses;

import lombok.*;
import vn.edu.iuh.fit.cineticketmanagebe.models.User;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private User user;
    private String message;
    private int code;
}
