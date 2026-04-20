package com.giftyrani.loananalytics.controller;

import com.giftyrani.loananalytics.dto.AuthResponseDTO;
import com.giftyrani.loananalytics.dto.LoginRequestDTO;
import com.giftyrani.loananalytics.dto.RegisterRequestDTO;
import com.giftyrani.loananalytics.model.Role;
import com.giftyrani.loananalytics.model.User;
import com.giftyrani.loananalytics.repository.UserRepository;
import com.giftyrani.loananalytics.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register and login to get JWT token")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @Operation(summary = "Register a new user",
               description = "Creates a new user. Role values: ROLE_ADMIN, ROLE_ANALYST, ROLE_VIEWER")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponseDTO(null, null, null, "Username already exists"));
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponseDTO(
                token,
                user.getUsername(),
                user.getRole().name(),
                "User registered successfully"
        ));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token",
               description = "Returns a JWT token. Use it as: Authorization: Bearer <token>")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponseDTO(
                token,
                user.getUsername(),
                user.getRole().name(),
                "Login successful"
        ));
    }
}
