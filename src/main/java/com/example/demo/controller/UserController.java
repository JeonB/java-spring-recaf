package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller for handling user-related requests.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Display all users.
     *
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    /**
     * Display a form to register a new user.
     *
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    /**
     * Process the registration of a new user.
     *
     * @param user the user to register
     * @param result the binding result
     * @param redirectAttributes attributes to add to the redirect
     * @return the redirect URL
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, 
                               BindingResult result, 
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/register";
        }
        
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Username")) {
                result.rejectValue("username", "error.user", e.getMessage());
            } else if (e.getMessage().contains("Email")) {
                result.rejectValue("email", "error.user", e.getMessage());
            } else {
                result.rejectValue("username", "error.user", e.getMessage());
            }
            return "user/register";
        }
    }

    /**
     * Display a specific user.
     *
     * @param id the user ID
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        return userService.getUserById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "user/view";
                })
                .orElse("redirect:/users");
    }

    /**
     * Display a form to edit a user.
     *
     * @param id the user ID
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return userService.getUserById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "user/edit";
                })
                .orElse("redirect:/users");
    }

    /**
     * Process the update of a user.
     *
     * @param id the user ID
     * @param user the updated user details
     * @param result the binding result
     * @param redirectAttributes attributes to add to the redirect
     * @return the redirect URL
     */
    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id, 
                             @Valid @ModelAttribute("user") User user, 
                             BindingResult result, 
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/edit";
        }
        
        try {
            userService.updateUser(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            return "redirect:/users/" + id;
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Username")) {
                result.rejectValue("username", "error.user", e.getMessage());
            } else if (e.getMessage().contains("Email")) {
                result.rejectValue("email", "error.user", e.getMessage());
            } else {
                result.rejectValue("username", "error.user", e.getMessage());
            }
            return "user/edit";
        }
    }

    /**
     * Delete a user.
     *
     * @param id the user ID
     * @param redirectAttributes attributes to add to the redirect
     * @return the redirect URL
     */
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }

    /**
     * Display a login form.
     *
     * @return the view name
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    // In a real application, you would implement proper authentication
    // This is just a placeholder for demonstration purposes
}