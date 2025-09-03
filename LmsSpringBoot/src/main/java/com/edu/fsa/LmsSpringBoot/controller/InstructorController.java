package com.edu.fsa.LmsSpringBoot.controller;

import com.edu.fsa.LmsSpringBoot.model.Course;
import com.edu.fsa.LmsSpringBoot.model.Instructor;
import com.edu.fsa.LmsSpringBoot.model.User;
import com.edu.fsa.LmsSpringBoot.repository.CourseRepository;
import com.edu.fsa.LmsSpringBoot.repository.InstructorRepository;
import com.edu.fsa.LmsSpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class InstructorController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/instructor/dashboard")
    public String getInstructorDashboard(@AuthenticationPrincipal User authenticatedUser, Model model) {
        if (authenticatedUser == null) {
            return "redirect:/auth/login";
        }

        String currentUserId = authenticatedUser.getId();
        Instructor instructor = instructorRepository.findByUserId(currentUserId);
        List<Course> courses;

        model.addAttribute("instructorName", authenticatedUser.getFirstName() + " " + authenticatedUser.getLastName());
        model.addAttribute("instructorEmail", authenticatedUser.getEmail());
        model.addAttribute("welcomeMessage", "Welcome back, " + authenticatedUser.getFirstName() + "!");

        if (instructor != null) {
            courses = courseRepository.findByInstructorId(instructor.getInstructorId());
        } else {
            courses = Collections.emptyList();
        }

        model.addAttribute("courses", courses);
        return "instructor/dashboard";
    }
}
