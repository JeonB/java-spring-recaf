package com.example.demo.repository;

import com.example.demo.model.Board;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Post entity operations.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    /**
     * Find all posts belonging to a specific board.
     * 
     * @param board the board to find posts for
     * @return a list of posts
     */
    List<Post> findByBoard(Board board);
    
    /**
     * Find all posts belonging to a specific board with pagination.
     * 
     * @param board the board to find posts for
     * @param pageable pagination information
     * @return a page of posts
     */
    Page<Post> findByBoard(Board board, Pageable pageable);
    
    /**
     * Find all posts by a specific author.
     * 
     * @param author the author to find posts for
     * @return a list of posts
     */
    List<Post> findByAuthor(User author);
    
    /**
     * Find all posts by a specific author with pagination.
     * 
     * @param author the author to find posts for
     * @param pageable pagination information
     * @return a page of posts
     */
    Page<Post> findByAuthor(User author, Pageable pageable);
    
    /**
     * Find posts containing the given title.
     * 
     * @param title the title to search for
     * @param pageable pagination information
     * @return a page of posts
     */
    Page<Post> findByTitleContaining(String title, Pageable pageable);
    
    /**
     * Find posts containing the given content.
     * 
     * @param content the content to search for
     * @param pageable pagination information
     * @return a page of posts
     */
    Page<Post> findByContentContaining(String content, Pageable pageable);
    
    /**
     * Find posts by board and order by creation date descending.
     * 
     * @param board the board to find posts for
     * @param pageable pagination information
     * @return a page of posts
     */
    Page<Post> findByBoardOrderByCreatedAtDesc(Board board, Pageable pageable);
    
    /**
     * Find the most viewed posts.
     * 
     * @param pageable pagination information
     * @return a page of posts
     */
    Page<Post> findAllByOrderByViewCountDesc(Pageable pageable);
}