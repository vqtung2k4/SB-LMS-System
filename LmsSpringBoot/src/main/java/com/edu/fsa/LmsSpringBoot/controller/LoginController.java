package com.edu.fsa.LmsSpringBoot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    @GetMapping("/auth/login")
    public String getLogin(Model model) {
        return "auth/login";
    }

    @PostMapping("/auth/login")
    public String processLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "Email is required");
            model.addAttribute("email", email);
            return "auth/login";
        }

        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Password is required");
            model.addAttribute("password", password);
            return "auth/login";
        }

        if("admin@example.com".equals(email) && "admin123".equals(password)) {
            redirectAttributes.addFlashAttribute("message","Login successful");
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Wrong email or password");
            model.addAttribute("email", email);
            return "auth/login";
        }
    }
}
