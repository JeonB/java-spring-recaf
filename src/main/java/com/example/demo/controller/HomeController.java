package com.example.demo.controller;

import com.example.demo.model.Board;
import com.example.demo.model.Post;
import com.example.demo.service.BoardService;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller for the home page.
 */
@Controller
public class HomeController {

    private final BoardService boardService;
    private final PostService postService;

    @Autowired
    public HomeController(BoardService boardService, PostService postService) {
        this.boardService = boardService;
        this.postService = postService;
    }

    /**
     * Display the home page with recent posts and boards.
     *
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/")
    public String home(Model model) {
        // Get all boards
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        
        // Get recent posts
        List<Post> recentPosts = postService.getAllPosts(
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")))
                .getContent();
        model.addAttribute("recentPosts", recentPosts);
        
        // Get most viewed posts
        List<Post> popularPosts = postService.getMostViewedPosts(
                PageRequest.of(0, 5))
                .getContent();
        model.addAttribute("popularPosts", popularPosts);
        
        return "home";
    }
}