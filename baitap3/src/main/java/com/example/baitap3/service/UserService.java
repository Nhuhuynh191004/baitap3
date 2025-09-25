package com.example.baitap3.service;

import com.example.baitap3.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Integer id);
    Optional<User> login(String email, String password);
    Optional<User> getUserByEmail(String email);
    Optional<User> findByEmail(String email);
    User createUser(User user);
    User updateUser(User user);
    User updateUserStatus(Integer id, Integer status);
}
