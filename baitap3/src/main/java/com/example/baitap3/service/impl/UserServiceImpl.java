package com.example.baitap3.service.impl;

import com.example.baitap3.entity.Role;
import com.example.baitap3.entity.User;
import com.example.baitap3.repository.RoleRepository;
import com.example.baitap3.repository.UserRepository;
import com.example.baitap3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // <- dùng interface, không dùng BCryptPasswordEncoder trực tiếp

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // So sánh password đã mã hóa
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public User createUser(User user) {
        // Hash password trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Gán role USER mặc định
        Role userRole = roleRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.setRoles(new HashSet<>());
        user.getRoles().add(userRole);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        Optional<User> existing = userRepository.findById(user.getId());
        if (existing.isEmpty()) throw new RuntimeException("User not found");
        User u = existing.get();
        u.setFull_name(user.getFull_name());
        u.setEmail(user.getEmail());
        u.setAddress(user.getAddress());
        u.setPhone(user.getPhone());
        return userRepository.save(u);
    }

    @Override
    public User updateUserStatus(Integer id, Integer status) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        u.setStatus(status);
        return userRepository.save(u);
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
