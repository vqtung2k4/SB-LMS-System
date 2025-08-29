package com.edu.fsa.LmsSpringBoot.repository.impl;

import com.edu.fsa.LmsSpringBoot.model.User;
import com.edu.fsa.LmsSpringBoot.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
    TypedQuery<User> query = entityManager.createQuery("FROM User", User.class);
    return query.getResultList();
    }

    @Override
    public User findById(String id) {
        return entityManager.find(User.class,id);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    @Override
    public User update(User user) {
        return entityManager.merge(user);
    }

    @Override
    public void deleteById(String id) {
        User user = findById(id);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
