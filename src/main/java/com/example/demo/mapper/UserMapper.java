package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper interface for User entity operations.
 * This provides an alternative to the JPA repository.
 */
@Mapper
public interface UserMapper {
    
    /**
     * Find all users.
     * 
     * @return a list of all users
     */
    List<User> findAll();
    
    /**
     * Find a user by ID.
     * 
     * @param id the user ID
     * @return the user if found
     */
    User findById(@Param("id") Long id);
    
    /**
     * Find a user by username.
     * 
     * @param username the username to search for
     * @return the user if found
     */
    User findByUsername(@Param("username") String username);
    
    /**
     * Find a user by email.
     * 
     * @param email the email to search for
     * @return the user if found
     */
    User findByEmail(@Param("email") String email);
    
    /**
     * Insert a new user.
     * 
     * @param user the user to insert
     * @return the number of rows affected
     */
    int insert(User user);
    
    /**
     * Update an existing user.
     * 
     * @param user the user to update
     * @return the number of rows affected
     */
    int update(User user);
    
    /**
     * Delete a user by ID.
     * 
     * @param id the user ID
     * @return the number of rows affected
     */
    int deleteById(@Param("id") Long id);
}