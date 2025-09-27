package com.example.baitap3.controller;

import com.example.baitap3.security.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // Xác thực user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Sinh token có cả roles
            String token = jwtUtil.generateToken(userDetails);

            // Trả về email + roles + token
            // Chú ý map lại authorities sang "ROLE_XXX" để thống nhất với SecurityConfig
            List<String> rolesWithPrefix = userDetails.getAuthorities().stream()
                    .map(auth -> auth.getAuthority()) // đã là ROLE_XXX
                    .toList();

            return ResponseEntity.ok(
                    new AuthResponse(
                            userDetails.getUsername(),
                            rolesWithPrefix,
                            token
                    )
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}

@Data
class AuthRequest {
    private String email;
    private String password;
}

@Data
class AuthResponse {
    private final String email;
    private final List<String> roles;
    private final String token;
}
