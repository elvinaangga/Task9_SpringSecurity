package com.task9_springsecurity.service;

import com.task9_springsecurity.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();
    Optional<Role> findById(Long id);
    void save(Role role);
    void update(Role role);
    void delete(Long id);
}
