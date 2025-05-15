package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for authentication-related requests.
 */
@Controller
public class AuthController {

    /**
     * Display the login page.
     *
     * @param error error message if authentication failed
     * @param logout logout message if user logged out
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully");
        }

        return "user/login";
    }
}