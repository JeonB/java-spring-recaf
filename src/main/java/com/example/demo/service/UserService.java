package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for User operations.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get all users.
     *
     * @return list of all users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get a user by ID.
     *
     * @param id the user ID
     * @return an Optional containing the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get a user by username.
     *
     * @param username the username
     * @return an Optional containing the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Get a user by email.
     *
     * @param email the email
     * @return an Optional containing the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Register a new user.
     *
     * @param user the user to register
     * @return the registered user
     * @throws IllegalArgumentException if the username or email is already taken
     */
    @Transactional
    public User registerUser(User user) {
        // Check if username is already taken
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken: " + user.getUsername());
        }

        // Check if email is already taken
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use: " + user.getEmail());
        }

        // Set creation time
        user.setCreatedAt(LocalDateTime.now());

        // In a real application, you would hash the password here
        // For example: user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Update a user's information.
     *
     * @param id the ID of the user to update
     * @param userDetails the updated user details
     * @return the updated user
     * @throws IllegalArgumentException if the user doesn't exist or if the new username/email is already taken
     */
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // Check if username is being changed and if the new username is already taken
        if (!user.getUsername().equals(userDetails.getUsername()) && 
                userRepository.existsByUsername(userDetails.getUsername())) {
            throw new IllegalArgumentException("Username is already taken: " + userDetails.getUsername());
        }

        // Check if email is being changed and if the new email is already taken
        if (!user.getEmail().equals(userDetails.getEmail()) && 
                userRepository.existsByEmail(userDetails.getEmail())) {
            throw new IllegalArgumentException("Email is already in use: " + userDetails.getEmail());
        }

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        
        // Only update password if it's provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            // In a real application, you would hash the password here
            // For example: user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            user.setPassword(userDetails.getPassword());
        }

        return userRepository.save(user);
    }

    /**
     * Delete a user by ID.
     *
     * @param id the ID of the user to delete
     * @throws IllegalArgumentException if the user doesn't exist
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Update a user's last login time.
     *
     * @param id the ID of the user
     * @throws IllegalArgumentException if the user doesn't exist
     */
    @Transactional
    public void updateLastLogin(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }
}