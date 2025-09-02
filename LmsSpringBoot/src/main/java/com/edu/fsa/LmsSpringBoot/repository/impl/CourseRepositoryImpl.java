package com.edu.fsa.LmsSpringBoot.repository.impl;

import com.edu.fsa.LmsSpringBoot.model.Course;
import com.edu.fsa.LmsSpringBoot.repository.CourseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
@Transactional
public class CourseRepositoryImpl implements CourseRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Course> findAll() {
        TypedQuery<Course> query = entityManager.createQuery("FROM Course", Course.class);
        return query.getResultList();
    }

    @Override
    public Course findById(String id) {
        return entityManager.find(Course.class, id);
    }

    @Override
    public Course save(Course course) {
        if (course.getCourseId() == null || course.getCourseId().isBlank()) {
            entityManager.persist(course);
            return course;
        } else {
            return entityManager.merge(course);
        }
    }

    @Override
    public Course update(Course course) {
        return entityManager.merge(course);
    }

    @Override
    public void deleteById(String id) {
        Course course = findById(id);
        if (course != null) {
            entityManager.remove(course);
        }
    }

    @Override
    public List<Course> findByInstructorId(String instructorId) {
        TypedQuery<Course> query = entityManager.createQuery("FROM Course c WHERE c.instructorId = :instructorId", Course.class);
        query.setParameter("instructorId", instructorId);
        return query.getResultList();
    }
}
