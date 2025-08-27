package com.task9_springsecurity.dao;

import com.task9_springsecurity.model.User;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    void save(User user);
    void update(User user);
    void delete(Long id);
}