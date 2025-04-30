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
 * 게시글(Post) 관련 주요 비즈니스 로직을 담당하는 서비스 클래스입니다.
 * 게시글의 생성, 조회, 수정, 삭제와 같은 트랜잭션 단위의 핵심 작업을 수행하며,
 * Board(게시판)와 User(작성자)와의 관계 처리를 담당합니다.
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
     * 전체 게시글 목록을 조회합니다.
     * @return 전체 게시글 리스트
     */
    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /**
     * 페이지네이션을 적용하여 전체 게시글 목록을 조회합니다.
     * @param pageable 페이징/정렬 정보
     * @return 게시글 페이지 객체
     */
    @Transactional(readOnly = true)
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /**
     * 특정 ID를 가진 게시글을 조회합니다.
     * @param id 게시글 ID
     * @return 해당 게시글(Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    /**
     * 특정 게시판(board) ID로 등록된 게시글 리스트를 조회합니다.
     * @param boardId 게시판 ID
     * @return 해당 게시판의 게시글 리스트
     * @throws IllegalArgumentException 존재하지 않는 게시판일 때
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsByBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
        return postRepository.findByBoard(board);
    }

    /**
     * 특정 게시판(board)에서 페이지네이션 적용된 게시글을 조회합니다.
     * @param boardId 게시판 ID
     * @param pageable 페이징/정렬 정보
     * @return 해당 게시판의 게시글 페이지 객체
     * @throws IllegalArgumentException 존재하지 않는 게시판일 때
     */
    @Transactional(readOnly = true)
    public Page<Post> getPostsByBoard(Long boardId, Pageable pageable) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
        return postRepository.findByBoard(board, pageable);
    }

    /**
     * 특정 작성자(author)의 게시글을 모두 조회합니다.
     * @param authorId 작성자 ID
     * @return 해당 작성자의 게시글 리스트
     * @throws IllegalArgumentException 존재하지 않는 사용자인 경우
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsByAuthor(Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + authorId));
        return postRepository.findByAuthor(author);
    }

    /**
     * 특정 작성자(author)의 게시글을 페이지네이션과 함께 조회합니다.
     * @param authorId 작성자 ID
     * @param pageable 페이징 정보
     * @return 해당 작성자의 게시글 페이지 객체
     * @throws IllegalArgumentException 존재하지 않는 사용자인 경우
     */
    @Transactional(readOnly = true)
    public Page<Post> getPostsByAuthor(Long authorId, Pageable pageable) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + authorId));
        return postRepository.findByAuthor(author, pageable);
    }

    /**
     * 게시글 제목 내 텍스트 검색(부분일치) 및 페이지네이션 결과를 반환합니다.
     * @param title 검색어(제목 일부)
     * @param pageable 페이징 정보
     * @return 검색 결과 페이지
     */
    @Transactional(readOnly = true)
    public Page<Post> searchPostsByTitle(String title, Pageable pageable) {
        return postRepository.findByTitleContaining(title, pageable);
    }

    /**
     * 게시글 내용 내 텍스트 검색(부분일치) 및 페이지네이션 결과를 반환합니다.
     * @param content 검색어(내용 일부)
     * @param pageable 페이징 정보
     * @return 검색 결과 페이지
     */
    @Transactional(readOnly = true)
    public Page<Post> searchPostsByContent(String content, Pageable pageable) {
        return postRepository.findByContentContaining(content, pageable);
    }

    /**
     * 가장 많이 조회된 게시글 목록을 페이지네이션을 적용해 조회합니다.
     * @param pageable 페이징 정보
     * @return 조회수 내림차순으로 정렬된 게시글 페이지
     */
    @Transactional(readOnly = true)
    public Page<Post> getMostViewedPosts(Pageable pageable) {
        return postRepository.findAllByOrderByViewCountDesc(pageable);
    }

    /**
     * 새 게시글을 생성합니다.
     * 게시글과 연결할 게시판, 작성자 정보를 검증 & 할당합니다.
     *
     * @param post 저장할 게시글 엔티티(제목, 내용 등)
     * @param boardId 게시글이 올라갈 게시판 ID
     * @param authorId 작성자(사용자) ID
     * @return 저장된 게시글
     * @throws IllegalArgumentException 게시판 또는 작성자가 없을 때
     */
    @Transactional
    public Post createPost(Post post, Long boardId, Long authorId) {
        // 게시판 존재 여부 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
        // 작성자 존재 여부 확인
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + authorId));
        // 관계 설정 및 기본값 초기화
        post.setBoard(board);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        post.setViewCount(0); // 새 글이므로 초기 조회수 설정
        return postRepository.save(post);
    }

    /**
     * 기존 게시글의 제목, 내용 등 주요 정보를 갱신합니다.
     * @param id 수정할 게시글의 ID
     * @param postDetails 변경할 정보가 담긴 엔티티
     * @return 갱신된 게시글
     * @throws IllegalArgumentException 게시글이 없을 때
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
     * 게시글을 ID로 삭제합니다.
     * @param id 삭제할 게시글 ID
     * @throws IllegalArgumentException 존재하지 않는 게시글일 때
     */
    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    /**
     * 게시글의 조회수(view count)를 1 증가시킵니다.
     * 사용자가 게시글을 열람할 때마다 호출됩니다.
     * @param id 게시글 ID
     * @return 갱신된 게시글
     * @throws IllegalArgumentException 게시글이 존재하지 않을 때
     */
    @Transactional
    public Post incrementViewCount(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        post.incrementViewCount();
        return postRepository.save(post);
    }
}