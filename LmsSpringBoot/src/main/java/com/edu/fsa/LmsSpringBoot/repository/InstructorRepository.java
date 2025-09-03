package com.edu.fsa.LmsSpringBoot.repository;

import com.edu.fsa.LmsSpringBoot.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, String> {

    Optional<Instructor> findByUserId(String userId);

    Optional<Instructor> findByInstructorId(String instructorId);
}
