package com.hunmin.domain.repository;

import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.WordScore;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordScoreRepository extends JpaRepository<WordScore, Long> {
    List<WordScore> findTop100ByOrderByTestRankScoreDescTestLevelDescCreatedAtAsc();   // 전체 랭킹 순위

    List<WordScore> findByMember_MemberIdOrderByCreatedAtDesc(Long memberId);
}
