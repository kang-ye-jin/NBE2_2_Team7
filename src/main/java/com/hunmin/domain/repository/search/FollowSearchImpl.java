package com.hunmin.domain.repository.search;

import com.hunmin.domain.dto.follow.FollowRequestDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.hunmin.domain.entity.QFollow.follow;
import static com.hunmin.domain.entity.QMember.member;

@Log4j2
public class FollowSearchImpl implements FollowSearch {

    private final JPQLQueryFactory queryFactory;

    public FollowSearchImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FollowRequestDTO> getFollowPage(Long memberId, Pageable pageable) {

        List<FollowRequestDTO> results = queryFactory
                .select(Projections.bean(FollowRequestDTO.class,
                        follow.followId,
                        follow.follower.memberId.as("followerId"),
                        follow.followee.memberId.as("followeeId"),
                        follow.notification,
                        follow.isBlock,
                        follow.createdAt)).distinct()
                .from(member)
                .join(member.followees, follow)
                .where(member.memberId.eq(memberId), follow.isNotNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        log.info("results{}",results.toArray());

        Long total = queryFactory
                .select(follow.count())
                .from(member)
                .leftJoin(member.followees, follow)
                .where(member.memberId.eq(memberId), follow.isNotNull())
                .fetchOne();
        log.info("total{}",total);

        long totalCount = total == null? 0L:total;

        return new PageImpl<>(results, pageable, totalCount);
    }
}
