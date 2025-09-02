package com.edu.fsa.LmsSpringBoot.controller;

import com.edu.fsa.LmsSpringBoot.model.Course;
import com.edu.fsa.LmsSpringBoot.model.Instructor;
import com.edu.fsa.LmsSpringBoot.repository.CourseRepository;
import com.edu.fsa.LmsSpringBoot.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminCourseController {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @GetMapping("/admin/course_management")
    public String getCourseManagement(Model model) {
        List<Course> courses = courseRepository.findAll();
        List<Instructor> instructors = instructorRepository.findAll();

        updateAdminCourseStatistic(model, courses);

        model.addAttribute("courses", courses);
        model.addAttribute("instructors", instructors); 
        return "admin/course_management";
    }

    private void updateAdminCourseStatistic(Model model, List<Course> courses) {
        long totalCourses = courses.size();
        long activeCourses = courses.stream().filter(Course::isActive).count();
        long draftCourses = courses.stream().filter(course -> !course.isActive()).count();

        long totalEnrolledStudents = 0; 

        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("activeCourses", activeCourses);
        model.addAttribute("draftCourses", draftCourses);
        model.addAttribute("totalEnrolled", totalEnrolledStudents);
    }

    @PostMapping("/admin/course_management")
    public String createCourse(@RequestParam("title") String title,
                               @RequestParam("description") String description,
                               @RequestParam("teacherId") String teacherId,
                               @RequestParam("maxStudents") int maxStudents,
                               @RequestParam("category") String category, 
                               @RequestParam("status") String status,
                               RedirectAttributes redirectAttributes) {
        try {
            Course course = new Course();
            course.setCourseId("COU" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase());
            course.setCourseName(title);
            course.setDescription(description);
            course.setInstructorId(teacherId);
            course.setMaxCapacity(maxStudents);
            course.setCredits(3); 
            course.setCourseCode(category.isEmpty() ? "GEN" : category.substring(0, 3).toUpperCase() + UUID.randomUUID().toString().substring(0, 3).toUpperCase()); // Basic code generation
            course.setActive("active".equalsIgnoreCase(status));
            course.setCreatedAt(LocalDateTime.now());

            courseRepository.save(course);
            redirectAttributes.addFlashAttribute("message", "Course created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error creating course: " + e.getMessage());
        }
        return "redirect:/admin/course_management";
    }

    @PostMapping("/admin/course_management/{courseId}/edit")
    public String editCourse(@PathVariable("courseId") String courseId,
                             @RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam("teacherId") String teacherId,
                             @RequestParam("maxStudents") int maxStudents,
                             @RequestParam("category") String category, // Can be used for courseCode logic or ignored
                             @RequestParam("status") String status,
                             RedirectAttributes redirectAttributes) {
        try {
            Course course = courseRepository.findById(courseId);
            if (course != null) {
                course.setCourseName(title);
                course.setDescription(description);
                course.setInstructorId(teacherId);
                course.setMaxCapacity(maxStudents);
                course.setActive("active".equalsIgnoreCase(status));
                // Optionally update course code based on category if needed
                courseRepository.update(course);
                redirectAttributes.addFlashAttribute("message", "Course updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("message", "Course not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error updating course: " + e.getMessage());
        }
        return "redirect:/admin/course_management";
    }

    @PostMapping("/admin/course_management/{courseId}/delete")
    public String deleteCourse(@PathVariable("courseId") String courseId,
                               RedirectAttributes redirectAttributes) {
        try {
            courseRepository.deleteById(courseId);
            redirectAttributes.addFlashAttribute("message", "Course deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting course: " + e.getMessage());
        }
        return "redirect:/admin/course_management";
    }
}
