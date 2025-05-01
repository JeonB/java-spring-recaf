package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for User operations using MyBatis.
 * This service demonstrates how to use MyBatis alongside JPA.
 */
@Service
public class MyBatisUserService {

    private final UserMapper userMapper;
    
    @Autowired
    public MyBatisUserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    /**
     * Get all users.
     *
     * @return list of all users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }
    
    /**
     * Get a user by ID.
     *
     * @param id the user ID
     * @return an Optional containing the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        User user = userMapper.findById(id);
        return Optional.ofNullable(user);
    }
    
    /**
     * Get a user by username.
     *
     * @param username the username
     * @return an Optional containing the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        User user = userMapper.findByUsername(username);
        return Optional.ofNullable(user);
    }
    
    /**
     * Get a user by email.
     *
     * @param email the email
     * @return an Optional containing the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        User user = userMapper.findByEmail(email);
        return Optional.ofNullable(user);
    }
    
    /**
     * Register a new user using MyBatis.
     *
     * @param user the user to register
     * @return the registered user
     */
    @Transactional
    public User registerUser(User user) {
        // Set creation time
        user.setCreatedAt(LocalDateTime.now());
        
        // In a real application, you would hash the password here
        
        userMapper.insert(user);
        return user;
    }
    
    /**
     * Update a user's information.
     *
     * @param id the ID of the user to update
     * @param userDetails the updated user details
     * @return the updated user
     */
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User existingUser = userMapper.findById(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        
        userDetails.setId(id);
        userMapper.update(userDetails);
        return userDetails;
    }
    
    /**
     * Delete a user by ID.
     *
     * @param id the ID of the user to delete
     */
    @Transactional
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }
    
    /**
     * Update a user's last login time.
     *
     * @param id the ID of the user
     */
    @Transactional
    public void updateLastLogin(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        
        user.setLastLogin(LocalDateTime.now());
        userMapper.update(user);
    }
}