package com.hunmin.domain.service;

import com.hunmin.domain.entity.Comment;
import com.hunmin.domain.entity.LikeComment;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.CommentException;
import com.hunmin.domain.exception.LikeCommentException;
import com.hunmin.domain.exception.MemberException;
import com.hunmin.domain.repository.CommentRepository;
import com.hunmin.domain.repository.LikeCommentRepository;
import com.hunmin.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class LikeCommentService {
    private final LikeCommentRepository likeCommentRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    //좋아요 등록
    @Transactional
    public void createLikeComment(Long memberId, Long commentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.NOT_FOUND::get);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException.NOT_FOUND::get);

        likeCommentRepository.findByMemberAndComment(member, comment).ifPresentOrElse(
                likeComment -> {
                    throw LikeCommentException.NOT_CREATED.get();
                },
                () -> {
                    likeCommentRepository.save(LikeComment.builder().member(member).comment(comment).build());
                    comment.incrementLikeCount();
                }
        );
    }

    //좋아요 삭제
    @Transactional
    public void deleteLikeComment(Long memberId, Long commentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.NOT_FOUND::get);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException.NOT_FOUND::get);
        LikeComment likeComment = likeCommentRepository.findByMemberAndComment(member, comment).orElseThrow(LikeCommentException.NOT_FOUND::get);

        try {
            likeCommentRepository.delete(likeComment);
            comment.decrementLikeCount();
        } catch (Exception e) {
            throw LikeCommentException.NOT_DELETED.get();
        }
    }

    //좋아요 여부 확인
    public boolean isLikeComment(Long memberId, Long commentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.NOT_FOUND::get);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException.NOT_FOUND::get);

        return likeCommentRepository.existsByMemberAndComment(member, comment);
    }

    //좋아요 누른 사용자 목록 조회
    public List<String> getLikeCommentMembers(Long commentId) {
        List<Member> members = likeCommentRepository.findMembersByLikedCommentId(commentId);
        return members.stream()
                .map(Member::getNickname)
                .collect(Collectors.toList());
    }
}
