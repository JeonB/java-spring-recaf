package com.example.demo.repository;

import com.example.demo.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Board entity operations.
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    /**
     * Find a board by its name.
     * 
     * @param name the name of the board
     * @return an Optional containing the board if found, or empty if not found
     */
    Optional<Board> findByName(String name);
    
    /**
     * Check if a board with the given name exists.
     * 
     * @param name the name to check
     * @return true if a board with the name exists, false otherwise
     */
    boolean existsByName(String name);
}