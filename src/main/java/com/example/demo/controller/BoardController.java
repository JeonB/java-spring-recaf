package com.example.demo.controller;

import com.example.demo.model.Board;
import com.example.demo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for handling board-related requests.
 */
@Controller
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Display all boards.
     *
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping
    public String getAllBoards(Model model) {
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "board/list";
    }

    /**
     * Display a form to create a new board.
     *
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("board", new Board());
        return "board/create";
    }

    /**
     * Process the creation of a new board.
     *
     * @param board the board to create
     * @param result the binding result
     * @param redirectAttributes attributes to add to the redirect
     * @return the redirect URL
     */
    @PostMapping
    public String createBoard(@Valid @ModelAttribute("board") Board board, 
                              BindingResult result, 
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "board/create";
        }
        
        try {
            board.setCreatedAt(LocalDateTime.now());
            boardService.createBoard(board);
            redirectAttributes.addFlashAttribute("successMessage", "Board created successfully!");
            return "redirect:/boards";
        } catch (IllegalArgumentException e) {
            result.rejectValue("name", "error.board", e.getMessage());
            return "board/create";
        }
    }

    /**
     * Display a specific board.
     *
     * @param id the board ID
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/{id}")
    public String getBoard(@PathVariable Long id, Model model) {
        return boardService.getBoardById(id)
                .map(board -> {
                    model.addAttribute("board", board);
                    return "board/view";
                })
                .orElse("redirect:/boards");
    }

    /**
     * Display a form to edit a board.
     *
     * @param id the board ID
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return boardService.getBoardById(id)
                .map(board -> {
                    model.addAttribute("board", board);
                    return "board/edit";
                })
                .orElse("redirect:/boards");
    }

    /**
     * Process the update of a board.
     *
     * @param id the board ID
     * @param board the updated board details
     * @param result the binding result
     * @param redirectAttributes attributes to add to the redirect
     * @return the redirect URL
     */
    @PostMapping("/{id}")
    public String updateBoard(@PathVariable Long id, 
                              @Valid @ModelAttribute("board") Board board, 
                              BindingResult result, 
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "board/edit";
        }
        
        try {
            boardService.updateBoard(id, board);
            redirectAttributes.addFlashAttribute("successMessage", "Board updated successfully!");
            return "redirect:/boards";
        } catch (IllegalArgumentException e) {
            result.rejectValue("name", "error.board", e.getMessage());
            return "board/edit";
        }
    }

    /**
     * Delete a board.
     *
     * @param id the board ID
     * @param redirectAttributes attributes to add to the redirect
     * @return the redirect URL
     */
    @PostMapping("/{id}/delete")
    public String deleteBoard(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boardService.deleteBoard(id);
            redirectAttributes.addFlashAttribute("successMessage", "Board deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/boards";
    }
}