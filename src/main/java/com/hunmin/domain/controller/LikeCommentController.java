package com.hunmin.domain.controller;

import com.hunmin.domain.repository.MemberRepository;
import com.hunmin.domain.service.LikeCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likeComment")
@RequiredArgsConstructor
@Log4j2
public class LikeCommentController {

    private final LikeCommentService likeCommentService;
    private final MemberRepository memberRepository;

    //좋아요 등록
    @PostMapping("/{commentId}")
    public ResponseEntity<String> createLikeComment(@PathVariable Long commentId, Authentication authentication) {
        Long memberId = memberRepository.findByEmail(authentication.getName()).getMemberId();
        likeCommentService.createLikeComment(memberId, commentId);
        return ResponseEntity.ok().build();
    }

    //좋아요 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteLikeComment(@PathVariable Long commentId, Authentication authentication) {
        Long memberId = memberRepository.findByEmail(authentication.getName()).getMemberId();
        likeCommentService.deleteLikeComment(memberId, commentId);
        return ResponseEntity.ok().build();
    }

    //좋아요 여부 확인
    @GetMapping("/{commentId}/member/{memberId}")
    public ResponseEntity<Boolean> isLikedComment(@PathVariable Long commentId, @PathVariable Long memberId) {
        boolean isLikedComment = likeCommentService.isLikeComment(memberId, commentId);
        return ResponseEntity.ok(isLikedComment);
    }

    //좋아요 누른 사용자 목록 조회
    @GetMapping("/{commentId}/members")
    public ResponseEntity<List<String>> getLikeCommentMembers(@PathVariable Long commentId) {
        List<String> members = likeCommentService.getLikeCommentMembers(commentId);
        log.info("@@@@@@@@@@@@@@@@");
        log.info(members);
        return ResponseEntity.ok(members);
    }
}
