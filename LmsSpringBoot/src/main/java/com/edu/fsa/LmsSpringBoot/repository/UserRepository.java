package com.edu.fsa.LmsSpringBoot.repository;

import com.edu.fsa.LmsSpringBoot.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();
    User findById(String id);
    User save(User user);
    User update(User user);
    void deleteById(String id);
    Optional<User> findByEmail(String email);
}
