package com.edu.fsa.LmsSpringBoot.controller;

import com.edu.fsa.LmsSpringBoot.model.User;
import com.edu.fsa.LmsSpringBoot.model.Instructor;
import com.edu.fsa.LmsSpringBoot.repository.InstructorRepository;
import com.edu.fsa.LmsSpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/auth/register")
    public String showRegistrationForm(Model model) {
        return "auth/register";
    }

    @PostMapping("/auth/register")
    public String registerUser(@RequestParam("fullname") String fullname,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("role") String role,
                               RedirectAttributes redirectAttributes) {
        System.out.println("Attempting to register user: " + email);

        if (userRepository.findByEmail(email) != null) {
            System.out.println("Email already registered: " + email);
            redirectAttributes.addFlashAttribute("error", "Email already registered.");
            return "redirect:/auth/register";
        }

        User user = new User();
        String userId = "USR" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        user.setId(userId);
        user.setEmail(email);
        String username = (email != null && email.contains("@")) ? email.substring(0, email.indexOf("@")) : email;
        user.setUsername(username);

        user.setPassword(passwordEncoder.encode(password));

        String[] nameParts = fullname.trim().split("\\s+", 2);
        user.setFirstName(nameParts.length > 0 ? nameParts[0] : "");
        user.setLastName(nameParts.length > 1 ? nameParts[1] : "");

        String userType = switch (role.toLowerCase()) {
            case "student" -> "STUDENT";
            case "instructor" -> "INSTRUCTOR";
            default -> "STUDENT";
        };
        user.setUserType(userType);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        try {
            userRepository.save(user);

            if ("INSTRUCTOR".equals(userType)) {
                Instructor instructor = new Instructor();
                instructor.setInstructorId("INS" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase());
                instructor.setUserId(user.getId());
                instructor.setDepartment("General");
                instructor.setTitle("Instructor");
                instructor.setSalary(java.math.BigDecimal.ZERO);
                instructorRepository.save(instructor);
                System.out.println("Instructor saved to database for user: " + user.getEmail());
            }
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please log in.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Registration failed due to a server error.");
            return "redirect:/auth/register";
        }
    }
}