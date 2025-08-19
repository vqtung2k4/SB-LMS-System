package com.edu.fsa.LmsSpringBoot.repository;

import com.edu.fsa.LmsSpringBoot.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User findById(int id);
    User save(User user);
    User update(User user);
    void deleteById(int id);
}
