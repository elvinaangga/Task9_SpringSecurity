package com.task9_springsecurity.dao;

import com.task9_springsecurity.model.Role;

import java.util.Optional;
import java.util.List;

public interface RoleDao {
    void save(Role role);
    void update(Role role);
    void delete(Long id);
    Optional<Role> findById(Long id);
    List<Role> findAll();
    Optional<Role> findByName(String name);
}