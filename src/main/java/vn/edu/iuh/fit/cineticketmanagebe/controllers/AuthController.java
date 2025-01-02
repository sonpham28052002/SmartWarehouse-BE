package vn.edu.iuh.fit.cineticketmanagebe.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.cineticketmanagebe.dtos.responses.AuthResponse;
import vn.edu.iuh.fit.cineticketmanagebe.dtos.requests.AuthRequest;
import vn.edu.iuh.fit.cineticketmanagebe.models.User;
import vn.edu.iuh.fit.cineticketmanagebe.repositories.UserRepository;
import vn.edu.iuh.fit.cineticketmanagebe.servies.AuthService;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.register(authRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authentication(authRequest));
    }

    @GetMapping("/auth")
    public ResponseEntity<List<User>> index() {
        return ResponseEntity.ok(userRepository.findAll());
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
