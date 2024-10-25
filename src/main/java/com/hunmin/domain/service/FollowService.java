package com.hunmin.domain.service;

import com.hunmin.domain.dto.follow.FollowRequestDTO;
import com.hunmin.domain.dto.page.PageRequestDTO;
import com.hunmin.domain.entity.Follow;
import com.hunmin.domain.entity.FollowStatus;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.FollowException;
import com.hunmin.domain.exception.MemberException;
import com.hunmin.domain.repository.FollowRepository;
import com.hunmin.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    // 팔로워 등록
    public FollowRequestDTO register(String myEmail, Long memberId) {
        try {
            Member followee = memberRepository.findById(memberId).orElseThrow(MemberException.NOT_FOUND::get);
            Member owner = memberRepository.findByEmail(myEmail);

            if (followee.getMemberId().equals(owner.getMemberId())) {
                throw FollowException.IMPOSSIBLE_FOLLOW.get();
            }

            // 중복체크
            Optional<Follow> foundMember = followRepository.findByMemberId(owner.getMemberId(), memberId);
            if (foundMember.isPresent()) {
                throw FollowException.DUPLICATED_FOLLOW.get();
            }

            Follow follow = Follow.builder()
                    .follower(owner)
                    .followee(followee)
                    .isBlock(false)
                    .notification(true)
                    .status(FollowStatus.PENDING)
                    .build();

            return new FollowRequestDTO(followRepository.save(follow));
        } catch (RuntimeException e) {
            log.info("팔로우 등록 실패{}", e.getMessage());
            throw e;
        }
    }

    @Transactional
    // 팔로이 수락
    public FollowRequestDTO registerAccept(String myEmail, Long memberId) {
        try {
            Member followee = memberRepository.findById(memberId).orElseThrow(MemberException.NOT_FOUND::get);
            Member owner = memberRepository.findByEmail(myEmail);

            // 중복체크
            Optional<Follow> foundMember = followRepository.findByMemberId(memberId, owner.getMemberId());
            if (foundMember.isPresent()) {
                throw FollowException.DUPLICATED_FOLLOW.get();
            }

            Follow follow = Follow.builder()
                    .follower(followee)
                    .followee(owner)
                    .isBlock(false)
                    .status(FollowStatus.ACCEPTED)
                    .notification(true)
                    .build();
            Follow follower = followRepository.findByMemberId(owner.getMemberId(), memberId).get();
            follower.setStatus(FollowStatus.ACCEPTED);

            return new FollowRequestDTO(followRepository.save(follow));
        } catch (RuntimeException e) {
            log.info("팔로우 수락 실패{}", e.getMessage());
            throw e;
        }
    }

    // 팔로이 삭제
    public Boolean remove(String myEmail, Long memberId) {
        try {
            Member owner = memberRepository.findByEmail(myEmail);
            Follow foundMember = followRepository.findByMemberId(owner.getMemberId(), memberId)
                    .orElseThrow(FollowException.NOT_FOUND::get);

            followRepository.deleteById(foundMember.getFollowId());

            return true;
        } catch (RuntimeException e) {
            log.info("팔로이 삭제 실패 {}", e.getMessage());
            throw e;
        }
    }

    // 팔로이 리스트 조회
    public Page<FollowRequestDTO> readPage(PageRequestDTO pageRequestDTO, Long memberId) {
        try {
            Sort sort = Sort.by("followId").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);
            return followRepository.getFollowPage(memberId, pageable);
        } catch (RuntimeException e) {
            log.info("페이징 실패 {}", e.getMessage());
            throw e;
        }
    }

    // 알림 변경
    @Transactional
    public Boolean turnNotification(String myEmail, Long memberId) {
        try{
            Member owner = memberRepository.findByEmail(myEmail);
        Follow foundMember = followRepository.findByMemberId(owner.getMemberId(), memberId)
                .orElseThrow(FollowException.NOT_FOUND::get);

            foundMember.setNotification(!foundMember.getNotification());
            return true;

        }catch (RuntimeException e){
            log.info("알림 변경에 실패하였습니다. {}",e.getMessage());
            throw e;
        }
    }
    // 차단 상태 변경
    @Transactional
    public Boolean blockFollower(String myEmail, Long memberId) {
        try {
            Member owner = memberRepository.findByEmail(myEmail);
            Follow foundMember = followRepository.findByMemberId(owner.getMemberId(), memberId)
                    .orElseThrow(FollowException.NOT_FOUND::get);

            foundMember.setNotification(foundMember.getIsBlock());
            foundMember.setIsBlock(!foundMember.getIsBlock());
            return true;
        }catch (RuntimeException e) {
            log.info("상대방 차단에 실패하였습니다. {}",e.getMessage());
            throw e;
        }
    }

}
