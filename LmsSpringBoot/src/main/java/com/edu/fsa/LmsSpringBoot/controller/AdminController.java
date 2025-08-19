package com.edu.fsa.LmsSpringBoot.controller;

import com.edu.fsa.LmsSpringBoot.model.User;
import com.edu.fsa.LmsSpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
}
