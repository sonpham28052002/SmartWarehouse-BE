package vn.edu.iuh.fit.cineticketmanagebe.servies;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.cineticketmanagebe.dtos.requests.auth.AuthRequest;
import vn.edu.iuh.fit.cineticketmanagebe.dtos.responses.auth.AuthResponse;
import vn.edu.iuh.fit.cineticketmanagebe.enums.Rule;
import vn.edu.iuh.fit.cineticketmanagebe.mappers.UserMapper;
import vn.edu.iuh.fit.cineticketmanagebe.models.User;
import vn.edu.iuh.fit.cineticketmanagebe.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(AuthRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .rule(Rule.ADMIN)
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
                .build();
    }

    @Transactional
    public AuthResponse authentication(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getName(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByName(request.getName())
                .orElseThrow();

        String refreshToken = jwtService.generateRefreshToken(user);
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }

    @Transactional
    public AuthResponse refreshToken(AuthRequest authRequest) throws Exception {
        try {
            String userName = jwtService.extractUserName(authRequest.getRefreshToken());

            Optional<User> optionalUser = userRepository.findByName(userName);
            if (optionalUser.isEmpty()) {
                throw new Exception("User not found");
            }

            User user = optionalUser.get();
            if (jwtService.isRefreshTokenValid(authRequest.getRefreshToken(), user)) {
                String token = jwtService.generateToken(user);

                return AuthResponse.builder()
                        .token(token)
                        .refreshToken(authRequest.getRefreshToken())
                        .user(user)
                        .build();
            } else {
                throw new Exception("Invalid refresh token");
            }
        } catch (Exception exception) {
            throw new Exception("Token refresh failed: " + exception.getMessage());
        }
    }

}
