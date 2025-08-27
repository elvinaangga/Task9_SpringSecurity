package com.task9_springsecurity.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.task9_springsecurity.model.User;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    void save(User user);
    void update(User user);
    void delete(Long id);
}