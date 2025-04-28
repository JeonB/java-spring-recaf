package com.example.demo.controller;

import com.example.demo.model.Board;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.service.BoardService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller for handling post-related requests.
 */
@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final BoardService boardService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, BoardService boardService, UserService userService) {
        this.postService = postService;
        this.boardService = boardService;
        this.userService = userService;
    }

    /**
     * Display all posts with pagination.
     *
     * @param page the page number
     * @param size the page size
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping
    public String getAllPosts(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Page<Post> postPage = postService.getAllPosts(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("totalItems", postPage.getTotalElements());
        
        return "post/list";
    }

    /**
     * Display posts for a specific board with pagination.
     *
     * @param boardId the board ID
     * @param page the page number
     * @param size the page size
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/board/{boardId}")
    public String getPostsByBoard(@PathVariable Long boardId,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  Model model) {
        Optional<Board> boardOpt = boardService.getBoardById(boardId);
        if (!boardOpt.isPresent()) {
            return "redirect:/boards";
        }
        
        Board board = boardOpt.get();
        Page<Post> postPage = postService.getPostsByBoard(
                boardId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        
        model.addAttribute("board", board);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("totalItems", postPage.getTotalElements());
        
        return "post/board-posts";
    }

    /**
     * Display a form to create a new post.
     *
     * @param boardId the board ID
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long boardId, Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        
        if (boardId != null) {
            boardService.getBoardById(boardId).ifPresent(board -> model.addAttribute("selectedBoardId", board.getId()));
        }
        
        return "post/create";
    }

    /**
     * Process the creation of a new post.
     *
     * @param post the post to create
     * @param boardId the board ID
     * @param authorId the author ID
     * @param result the binding result
     * @param redirectAttributes attributes to add to the redirect
     * @return the redirect URL
     */
    @PostMapping
    public String createPost(@Valid @ModelAttribute("post") Post post,
                             @RequestParam Long boardId,
                             @RequestParam Long authorId,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "post/create";
        }
        
        try {
            Post savedPost = postService.createPost(post, boardId, authorId);
            redirectAttributes.addFlashAttribute("successMessage", "Post created successfully!");
            return "redirect:/posts/" + savedPost.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/posts/new?boardId=" + boardId;
        }
    }

    /**
     * Display a specific post.
     *
     * @param id the post ID
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Optional<Post> postOpt = postService.getPostById(id);
        if (!postOpt.isPresent()) {
            return "redirect:/posts";
        }
        
        Post post = postOpt.get();
        // Increment view count
        postService.incrementViewCount(id);
        
        model.addAttribute("post", post);
        return "post/view";
    }

    /**
     * Display a form to edit a post.
     *
     * @param id the post ID
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Post> postOpt = postService.getPostById(id);
        if (!postOpt.isPresent()) {
            return "redirect:/posts";
        }
        
        Post post = postOpt.get();
        model.addAttribute("post", post);
        
        return "post/edit";
    }

    /**
     * Process the update of a post.
     *
     * @param id the post ID
     * @param post the updated post details
     * @param result the binding result
     * @param redirectAttributes attributes to add to the redirect
     * @return the redirect URL
     */
    @PostMapping("/{id}")
    public String updatePost(@PathVariable Long id,
                             @Valid @ModelAttribute("post") Post post,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "post/edit";
        }
        
        try {
            postService.updatePost(id, post);
            redirectAttributes.addFlashAttribute("successMessage", "Post updated successfully!");
            return "redirect:/posts/" + id;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/posts/" + id + "/edit";
        }
    }

    /**
     * Delete a post.
     *
     * @param id the post ID
     * @param redirectAttributes attributes to add to the redirect
     * @return the redirect URL
     */
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Post> postOpt = postService.getPostById(id);
            if (postOpt.isPresent()) {
                Long boardId = postOpt.get().getBoard().getId();
                postService.deletePost(id);
                redirectAttributes.addFlashAttribute("successMessage", "Post deleted successfully!");
                return "redirect:/posts/board/" + boardId;
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Post not found");
                return "redirect:/posts";
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/posts";
        }
    }

    /**
     * Search posts by title or content.
     *
     * @param query the search query
     * @param page the page number
     * @param size the page size
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/search")
    public String searchPosts(@RequestParam String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Page<Post> titleResults = postService.searchPostsByTitle(
                query, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        
        Page<Post> contentResults = postService.searchPostsByContent(
                query, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        
        model.addAttribute("titleResults", titleResults.getContent());
        model.addAttribute("contentResults", contentResults.getContent());
        model.addAttribute("query", query);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalTitlePages", titleResults.getTotalPages());
        model.addAttribute("totalContentPages", contentResults.getTotalPages());
        
        return "post/search-results";
    }
}