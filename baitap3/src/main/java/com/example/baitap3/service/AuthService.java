package com.example.baitap3.service;

import com.example.baitap3.entity.User;
import com.example.baitap3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Bean BCryptPasswordEncoder


    public boolean login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }
}
