package com.example.baitap3.service.impl;

import com.example.baitap3.entity.Role;
import com.example.baitap3.entity.User;
import com.example.baitap3.repository.RoleRepository;
import com.example.baitap3.repository.UserRepository;
import com.example.baitap3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Mã hóa password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);

        // Gán role mặc định USER (id=2)
        Role userRole = roleRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Role USER không tồn tại"));
        user.setRoles(Set.of(userRole));

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUserStatus(Integer id, Integer status) {
        User exist = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        exist.setStatus(status);
        return userRepository.save(exist);
    }
}
