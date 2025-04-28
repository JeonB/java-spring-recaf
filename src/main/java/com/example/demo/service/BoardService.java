package com.example.demo.service;

import com.example.demo.model.Board;
import com.example.demo.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for Board operations.
 */
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * Get all boards.
     *
     * @return list of all boards
     */
    @Transactional(readOnly = true)
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    /**
     * Get a board by its ID.
     *
     * @param id the board ID
     * @return an Optional containing the board if found
     */
    @Transactional(readOnly = true)
    public Optional<Board> getBoardById(Long id) {
        return boardRepository.findById(id);
    }

    /**
     * Get a board by its name.
     *
     * @param name the board name
     * @return an Optional containing the board if found
     */
    @Transactional(readOnly = true)
    public Optional<Board> getBoardByName(String name) {
        return boardRepository.findByName(name);
    }

    /**
     * Create a new board.
     *
     * @param board the board to create
     * @return the created board
     * @throws IllegalArgumentException if a board with the same name already exists
     */
    @Transactional
    public Board createBoard(Board board) {
        if (boardRepository.existsByName(board.getName())) {
            throw new IllegalArgumentException("A board with name '" + board.getName() + "' already exists");
        }
        return boardRepository.save(board);
    }

    /**
     * Update an existing board.
     *
     * @param id the ID of the board to update
     * @param boardDetails the updated board details
     * @return the updated board
     * @throws IllegalArgumentException if the board doesn't exist or if the new name is already taken
     */
    @Transactional
    public Board updateBoard(Long id, Board boardDetails) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + id));

        // Check if name is being changed and if the new name is already taken
        if (!board.getName().equals(boardDetails.getName()) && 
                boardRepository.existsByName(boardDetails.getName())) {
            throw new IllegalArgumentException("A board with name '" + boardDetails.getName() + "' already exists");
        }

        board.setName(boardDetails.getName());
        board.setDescription(boardDetails.getDescription());

        return boardRepository.save(board);
    }

    /**
     * Delete a board by its ID.
     *
     * @param id the ID of the board to delete
     * @throws IllegalArgumentException if the board doesn't exist
     */
    @Transactional
    public void deleteBoard(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new IllegalArgumentException("Board not found with id: " + id);
        }
        boardRepository.deleteById(id);
    }
}