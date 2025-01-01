package vn.edu.iuh.fit.cineticketmanagebe.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.cineticketmanagebe.dtos.responses.AuthResponse;
import vn.edu.iuh.fit.cineticketmanagebe.dtos.requests.AuthRequest;
import vn.edu.iuh.fit.cineticketmanagebe.servies.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.register(authRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authentication(authRequest));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody AuthRequest authRequest) {
        try {
            return ResponseEntity.ok(authService.refreshToken(authRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message("Invalid or expired refresh token.")
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .build());
        }
    }
}
