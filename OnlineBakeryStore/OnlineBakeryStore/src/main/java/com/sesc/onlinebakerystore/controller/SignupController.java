package com.sesc.onlinebakerystore.controller;

import com.sesc.onlinebakerystore.model.Admin;
import com.sesc.onlinebakerystore.model.User;
import com.sesc.onlinebakerystore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("isLoggedIn", false);
        model.addAttribute("isAdmin", false);
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam("confirm_password") String confirmPassword,
            @RequestParam String fullname,
            @RequestParam String email,
            @RequestParam("telephon_no") String telephoneNo,
            @RequestParam(value = "isAdmin", defaultValue = "false") boolean isAdmin,
            Model model
    ) throws IOException {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match");
            model.addAttribute("isLoggedIn", false);
            model.addAttribute("isAdmin", false);
            return "signup";
        }

        User existingUser = userService.findByUsername(username);
        if (existingUser != null) {
            model.addAttribute("errorMessage", "Username already exists");
            model.addAttribute("isLoggedIn", false);
            model.addAttribute("isAdmin", false);
            return "signup";
        }

        User user = isAdmin ? new Admin(username, email, password, fullname, telephoneNo, List.of("MANAGE_USERS"))
                : new User(username, email, password, fullname, telephoneNo, false);
        userService.saveUser(user);
        return "redirect:/login";
    }
}