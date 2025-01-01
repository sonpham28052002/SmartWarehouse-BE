package vn.edu.iuh.fit.cineticketmanagebe.dtos.requests;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    private Long id;
    private String name;
    private String lastName;
    private String firstName;
    private String password;
    private String token;
    private String refreshToken;
}
