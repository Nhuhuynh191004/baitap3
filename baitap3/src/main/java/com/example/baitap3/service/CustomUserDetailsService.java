package com.example.baitap3.service;

import com.example.baitap3.entity.User;
import com.example.baitap3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service cung cấp thông tin User cho Spring Security
 * Khi login, Spring Security sẽ gọi loadUserByUsername(email)
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Tìm user theo email
     * @param email email của user
     * @return User implements UserDetails
     * @throws UsernameNotFoundException nếu không tìm thấy user
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tìm user trong database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Trả về entity User đã implement UserDetails
        // Spring Security sẽ dùng User này để xác thực và phân quyền
        return user;
    }
}
