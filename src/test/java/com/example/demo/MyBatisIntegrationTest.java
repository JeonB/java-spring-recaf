package com.example.demo;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.MyBatisUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for MyBatis functionality.
 * Tests that MyBatis is properly configured and can coexist with JPA.
 */
@SpringBootTest
@Transactional
public class MyBatisIntegrationTest {

    @Autowired
    private MyBatisUserService myBatisUserService;
    
    @Autowired
    private UserMapper userMapper;
    
    @Test
    public void testMyBatisConfiguration() {
        // Verify that MyBatis components are properly autowired
        assertNotNull(myBatisUserService, "MyBatisUserService should be autowired");
        assertNotNull(userMapper, "UserMapper should be autowired");
    }
    
    @Test
    public void testCreateAndRetrieveUser() {
        // Create a test user
        User user = new User("testuser", "password123", "test@example.com");
        
        // Save the user using MyBatis
        User savedUser = myBatisUserService.registerUser(user);
        
        // Verify the user was saved and has an ID
        assertNotNull(savedUser.getId(), "User ID should not be null after saving");
        
        // Retrieve the user using MyBatis
        Optional<User> retrievedUser = myBatisUserService.getUserById(savedUser.getId());
        
        // Verify the user was retrieved
        assertTrue(retrievedUser.isPresent(), "User should be retrieved by ID");
        assertEquals("testuser", retrievedUser.get().getUsername(), "Username should match");
        assertEquals("test@example.com", retrievedUser.get().getEmail(), "Email should match");
    }
    
    @Test
    public void testFindByUsername() {
        // Create a test user
        User user = new User("findbyusername", "password123", "findbyusername@example.com");
        myBatisUserService.registerUser(user);
        
        // Find by username
        Optional<User> foundUser = myBatisUserService.getUserByUsername("findbyusername");
        
        // Verify the user was found
        assertTrue(foundUser.isPresent(), "User should be found by username");
        assertEquals("findbyusername@example.com", foundUser.get().getEmail(), "Email should match");
    }
    
    @Test
    public void testUpdateUser() {
        // Create a test user
        User user = new User("updateuser", "password123", "updateuser@example.com");
        User savedUser = myBatisUserService.registerUser(user);
        
        // Update the user
        User updatedDetails = new User("updateuser", "newpassword", "newemail@example.com");
        updatedDetails.setId(savedUser.getId());
        
        User updatedUser = myBatisUserService.updateUser(savedUser.getId(), updatedDetails);
        
        // Verify the user was updated
        assertEquals("newemail@example.com", updatedUser.getEmail(), "Email should be updated");
        
        // Retrieve the user again to verify the update was persisted
        Optional<User> retrievedUser = myBatisUserService.getUserById(savedUser.getId());
        assertTrue(retrievedUser.isPresent(), "User should still exist after update");
        assertEquals("newemail@example.com", retrievedUser.get().getEmail(), "Updated email should be persisted");
    }
    
    @Test
    public void testDeleteUser() {
        // Create a test user
        User user = new User("deleteuser", "password123", "deleteuser@example.com");
        User savedUser = myBatisUserService.registerUser(user);
        
        // Verify the user exists
        Optional<User> userBeforeDelete = myBatisUserService.getUserById(savedUser.getId());
        assertTrue(userBeforeDelete.isPresent(), "User should exist before deletion");
        
        // Delete the user
        myBatisUserService.deleteUser(savedUser.getId());
        
        // Verify the user no longer exists
        Optional<User> userAfterDelete = myBatisUserService.getUserById(savedUser.getId());
        assertFalse(userAfterDelete.isPresent(), "User should not exist after deletion");
    }
    
    @Test
    public void testFindAllUsers() {
        // Create some test users
        myBatisUserService.registerUser(new User("user1", "password1", "user1@example.com"));
        myBatisUserService.registerUser(new User("user2", "password2", "user2@example.com"));
        
        // Find all users
        List<User> users = myBatisUserService.getAllUsers();
        
        // Verify users were found
        assertFalse(users.isEmpty(), "User list should not be empty");
        assertTrue(users.size() >= 2, "There should be at least 2 users");
    }
}