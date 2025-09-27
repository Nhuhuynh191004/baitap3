package com.example.baitap3.service;

import com.example.baitap3.dto.LoginRequest;
import com.example.baitap3.dto.AuthResponse;
import com.example.baitap3.dto.LoginRequest;
import com.example.baitap3.dto.RegisterRequest;
import com.example.baitap3.dto.UserResponse;
import com.example.baitap3.entity.Role;
import com.example.baitap3.entity.User;
import com.example.baitap3.repository.RoleRepository;
import com.example.baitap3.repository.UserRepository;
import com.example.baitap3.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Register
    public UserResponse register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .phone(request.getPhone())
                .status(1)
                .build();

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getAddress(),
                savedUser.getPhone(),
                savedUser.getStatus(),
                savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }

    // Authenticate / login
    public AuthResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Tạo UserDetails từ entity User
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(
                        user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(
                                        role.getName().startsWith("ROLE_") ? role.getName() : "ROLE_" + role.getName()
                                ))
                                .toList()
                )
                .build();

        // Sinh token có cả roles
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token);
    }

}
