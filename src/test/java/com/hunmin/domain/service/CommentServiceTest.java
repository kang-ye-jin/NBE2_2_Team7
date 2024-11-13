//package com.hunmin.domain.service;
//
//import com.hunmin.domain.dto.comment.CommentRequestDTO;
//import com.hunmin.domain.dto.comment.CommentResponseDTO;
//import com.hunmin.domain.dto.page.PageRequestDTO;
//import com.hunmin.domain.entity.Board;
//import com.hunmin.domain.entity.Comment;
//import com.hunmin.domain.entity.Member;
//import com.hunmin.domain.repository.BoardRepository;
//import com.hunmin.domain.repository.CommentRepository;
//import com.hunmin.domain.repository.MemberRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//public class CommentServiceTest {
//
//    @InjectMocks
//    private CommentService commentService;
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @Mock
//    private BoardRepository boardRepository;
//
//    @Mock
//    private CommentRepository commentRepository;
//
//    private Member member;
//    private Board board;
//    private Comment comment;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        member = new Member(1L, "테스터");
//        board = new Board(1L, member, "테스트 제목", "테스트 내용");
//        comment = new Comment(1L, member, board, "테스트 댓글");
//    }
//
//    // 댓글 등록
//    @Test
//    public void testCreateComment() {
//        CommentRequestDTO commentRequestDTO = new CommentRequestDTO(null, null, 1L, "테스트 댓글");
//
//        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
//        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
//        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
//
//        CommentResponseDTO response = commentService.createComment(1L, commentRequestDTO);
//
//        assertNotNull(response);
//        assertEquals("테스트 댓글", response.getContent());
//        verify(commentRepository, times(1)).save(any(Comment.class));
//    }
//
//    // 대댓글 등록
//    @Test
//    public void testCreateCommentChild() {
//        CommentRequestDTO commentRequestDTO = new CommentRequestDTO(null, null, 1L, "테스트 대댓글");
//
//        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
//        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
//        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
//        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
//
//        CommentResponseDTO response = commentService.createCommentChild(1L, 1L, commentRequestDTO);
//
//        assertNotNull(response);
//        assertEquals("테스트 대댓글", response.getContent());
//        verify(commentRepository, times(1)).save(any(Comment.class));
//    }
//
//    // 댓글 수정
//    @Test
//    public void testUpdateComment() {
//        CommentRequestDTO commentRequestDTO = new CommentRequestDTO(null, null, null, "수정된 댓글");
//
//        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
//
//        CommentResponseDTO response = commentService.updateComment(1L, commentRequestDTO);
//
//        assertNotNull(response);
//        assertEquals("수정된 댓글", response.getContent());
//    }
//
//    // 댓글 삭제
//    @Test
//    public void testDeleteComment() {
//        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
//
//        CommentResponseDTO response = commentService.deleteComment(1L);
//
//        assertNotNull(response);
//        assertEquals("테스트 댓글", response.getContent());
//        verify(commentRepository, times(1)).delete(any(Comment.class));
//    }
//
//    // 게시글 별 댓글 목록 조회
//    @Test
//    public void testReadCommentList() {
//        Page<Comment> commentPage = new PageImpl<>(List.of(comment));
//        when(commentRepository.findByBoardId(eq(1L), any(Pageable.class))).thenReturn(commentPage);
//
//        Page<CommentResponseDTO> response = commentService.readCommentList(1L, PageRequestDTO.builder().build());
//
//        assertNotNull(response);
//        assertEquals(1, response.getTotalElements());
//    }
//}
