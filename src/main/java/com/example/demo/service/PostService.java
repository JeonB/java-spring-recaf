package com.example.demo.service;

import com.example.demo.model.Board;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for Post operations.
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, BoardRepository boardRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all posts.
     *
     * @return list of all posts
     */
    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /**
     * Get all posts with pagination.
     *
     * @param pageable pagination information
     * @return page of posts
     */
    @Transactional(readOnly = true)
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /**
     * Get a post by ID.
     *
     * @param id the post ID
     * @return an Optional containing the post if found
     */
    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    /**
     * Get posts by board.
     *
     * @param boardId the board ID
     * @return list of posts
     * @throws IllegalArgumentException if the board doesn't exist
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsByBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
        return postRepository.findByBoard(board);
    }

    /**
     * Get posts by board with pagination.
     *
     * @param boardId the board ID
     * @param pageable pagination information
     * @return page of posts
     * @throws IllegalArgumentException if the board doesn't exist
     */
    @Transactional(readOnly = true)
    public Page<Post> getPostsByBoard(Long boardId, Pageable pageable) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
        return postRepository.findByBoard(board, pageable);
    }

    /**
     * Get posts by author.
     *
     * @param authorId the author ID
     * @return list of posts
     * @throws IllegalArgumentException if the user doesn't exist
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsByAuthor(Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + authorId));
        return postRepository.findByAuthor(author);
    }

    /**
     * Get posts by author with pagination.
     *
     * @param authorId the author ID
     * @param pageable pagination information
     * @return page of posts
     * @throws IllegalArgumentException if the user doesn't exist
     */
    @Transactional(readOnly = true)
    public Page<Post> getPostsByAuthor(Long authorId, Pageable pageable) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + authorId));
        return postRepository.findByAuthor(author, pageable);
    }

    /**
     * Search posts by title.
     *
     * @param title the title to search for
     * @param pageable pagination information
     * @return page of posts
     */
    @Transactional(readOnly = true)
    public Page<Post> searchPostsByTitle(String title, Pageable pageable) {
        return postRepository.findByTitleContaining(title, pageable);
    }

    /**
     * Search posts by content.
     *
     * @param content the content to search for
     * @param pageable pagination information
     * @return page of posts
     */
    @Transactional(readOnly = true)
    public Page<Post> searchPostsByContent(String content, Pageable pageable) {
        return postRepository.findByContentContaining(content, pageable);
    }

    /**
     * Get most viewed posts.
     *
     * @param pageable pagination information
     * @return page of posts
     */
    @Transactional(readOnly = true)
    public Page<Post> getMostViewedPosts(Pageable pageable) {
        return postRepository.findAllByOrderByViewCountDesc(pageable);
    }

    /**
     * Create a new post.
     *
     * @param post the post to create
     * @param boardId the ID of the board to post to
     * @param authorId the ID of the post author
     * @return the created post
     * @throws IllegalArgumentException if the board or author doesn't exist
     */
    @Transactional
    public Post createPost(Post post, Long boardId, Long authorId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
        
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + authorId));
        
        post.setBoard(board);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        post.setViewCount(0);
        
        return postRepository.save(post);
    }

    /**
     * Update an existing post.
     *
     * @param id the ID of the post to update
     * @param postDetails the updated post details
     * @return the updated post
     * @throws IllegalArgumentException if the post doesn't exist
     */
    @Transactional
    public Post updatePost(Long id, Post postDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        
        return postRepository.save(post);
    }

    /**
     * Delete a post by ID.
     *
     * @param id the ID of the post to delete
     * @throws IllegalArgumentException if the post doesn't exist
     */
    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    /**
     * Increment the view count of a post.
     *
     * @param id the ID of the post
     * @return the updated post
     * @throws IllegalArgumentException if the post doesn't exist
     */
    @Transactional
    public Post incrementViewCount(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        
        post.incrementViewCount();
        return postRepository.save(post);
    }
}