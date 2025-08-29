package com.edu.fsa.LmsSpringBoot.controller;

import com.edu.fsa.LmsSpringBoot.model.User;
import com.edu.fsa.LmsSpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin/dashboard")
    public String getAdminDashboard(Model model) {
        updateAdminStatistic(model);
        return "admin/dashboard";
    }

    @GetMapping("/admin/user_management")
        public String getAdminUserManagement(Model model) {
        updateAdminStatistic(model);

        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user_management";
    }

    private void updateAdminStatistic(Model model) {
        List<User> users = userRepository.findAll();

        long totalUsers = users.size();
        long activeUsers = users.stream().filter(User::isActive).count();
        long adminUsers = users.stream().filter(user -> "ADMIN".equalsIgnoreCase(user.getUserType())).count();
        long teacherUsers = users.stream().filter(user -> "INSTRUCTOR".equalsIgnoreCase(user.getUserType())).count();
        long studentUsers = users.stream().filter(user -> "STUDENT".equalsIgnoreCase(user.getUserType())).count();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("adminUsers", adminUsers);
        model.addAttribute("teacherUsers", teacherUsers);
        model.addAttribute("studentUsers", studentUsers);
    }

    @PostMapping("/admin/user_management")
    public String createUser(@RequestParam("name") String name,
                             @RequestParam("email") String email,
                             @RequestParam("password") String password,
                             @RequestParam("role") String role,
                             RedirectAttributes redirectAttributes) {
        String[] parts = name.trim().split("\\s+", 2);
        String firstName = parts.length > 0 ? parts[0] : "";
        String lastName = parts.length > 1 ? parts[1] : "";

        String userType = switch (role == null ? "" : role.toLowerCase()) {
            case "admin" -> "ADMIN";
            case "teacher" -> "INSTRUCTOR";
            case "student" -> "STUDENT";
            default -> "STUDENT";
        };

        String username = (email != null && email.contains("@")) ? email.substring(0, email.indexOf("@")) : email;
        String userId = "USR" + UUID.randomUUID().toString().replace("-","").substring(0,6).toUpperCase();

        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUserType(userType);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "User created successfully");
        return "redirect:/admin/user_management";
    }

    @PostMapping("/admin/user_management/{userId}/delete")
    public String deleteUser(@PathVariable("userId") String userId,
                             RedirectAttributes redirectAttributes) {
        try {
            userRepository.deleteById(userId);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting user");
        }
        return "redirect:/admin/user_management";
    }
}
