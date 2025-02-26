package vn.edu.iuh.fit.smartwarehousebe.servies;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.auth.AuthRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.auth.AuthResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.Role;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.repositories.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${APP.JWT.EXPIRATION_TIME_ACCESS_TOKEN}")
    private long EXPIRATION_TIME_ACCESS_TOKEN;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {
        User user = User.builder()
                .fullName(request.getFullName())
                .code(UUID.randomUUID().toString().replace("-", "").substring(0, 12))
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();
        User newUser = userRepository.save(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", newUser.toString());


        String refreshToken = jwtService.generateRefreshToken(claims,newUser);
        String token = jwtService.generateToken(claims,newUser);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(newUser)
                .exp(new Date(System.currentTimeMillis() + EXPIRATION_TIME_ACCESS_TOKEN).getTime())
                .build();
    }

    public AuthResponse authentication(String userName, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName,
                        password
                )
        );
        User user = userRepository.findUserByUserName(userName)
                .orElseThrow();
        String refreshToken = jwtService.generateRefreshToken(user);
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(user)
                .exp(new Date(System.currentTimeMillis() + EXPIRATION_TIME_ACCESS_TOKEN).getTime())
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) throws Exception {
        try {
            String userName = jwtService.extractUserName(refreshToken);

            Optional<User> optionalUser = userRepository.findUserByUserName(userName);
            if (optionalUser.isEmpty()) {
                throw new Exception("User not found");
            }

            User user = optionalUser.get();
            if (jwtService.isRefreshTokenValid(refreshToken, user)) {
                String token = jwtService.generateToken(user);

                return AuthResponse.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .user(user)
                        .exp(new Date(System.currentTimeMillis() + EXPIRATION_TIME_ACCESS_TOKEN).getTime())
                        .build();
            } else {
                throw new Exception("Invalid refresh token");
            }
        } catch (Exception exception) {
            throw new Exception("Token refresh failed: " + exception.getMessage());
        }
    }

}
