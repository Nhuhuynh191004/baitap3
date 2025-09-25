package com.example.baitap3.service;

import com.example.baitap3.entity.Role;
import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role createRole(Role role);
}
