package com.example.baitap3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    private Integer status;
    private Set<String> roles; // tên các role, ví dụ: ROLE_ADMIN, ROLE_USER
}
