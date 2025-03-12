package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.auth.AuthRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.auth.AuthResponse;
import vn.edu.iuh.fit.smartwarehousebe.servies.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest authRequest) {
    return ResponseEntity.ok(authService.register(authRequest));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestParam("userName") String userName,
      @RequestParam("password") String password) {
    return ResponseEntity.ok(authService.authentication(userName, password));
  }

  @PostMapping("/refreshToken")
  public ResponseEntity<AuthResponse> refreshToken(@RequestBody String refreshToken) {
    try {
      return ResponseEntity.ok(authService.refreshToken(refreshToken));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(AuthResponse.builder()
              .message("Invalid or expired refresh token.")
              .code(HttpStatus.UNAUTHORIZED.value())
              .build());
    }
  }
}
