package com.edu.fsa.LmsSpringBoot.repository;

import com.edu.fsa.LmsSpringBoot.model.Course;

import java.util.List;

public interface CourseRepository {
    List<Course> findAll();
    Course findById(String id);
    Course save(Course course);
    Course update(Course course);
    void deleteById(String id);
}
