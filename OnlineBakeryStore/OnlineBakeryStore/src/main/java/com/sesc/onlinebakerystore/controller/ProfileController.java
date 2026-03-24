package com.sesc.onlinebakerystore.controller;

import com.sesc.onlinebakerystore.model.Admin;
import com.sesc.onlinebakerystore.model.User;
import com.sesc.onlinebakerystore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            return "redirect:/login";
        }
        try {
            User user = userService.findByUsername(username);
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("isLoggedIn", true);
                model.addAttribute("isAdmin", session.getAttribute("isAdmin") != null && (Boolean) session.getAttribute("isAdmin"));
                if (user.isAdmin()) {
                    model.addAttribute("permissions", ((Admin) user).getPermissions());
                }
                return "profile";
            } else {
                model.addAttribute("error", "User not found");
                return "redirect:/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error retrieving profile: " + e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/delete-account")
    public String deleteAccount(HttpSession session) throws IOException {
        String username = (String) session.getAttribute("loggedInUser");
        if (username != null) {
            userService.deleteUser(username);
            session.invalidate();
        }
        return "redirect:/login";
    }
}