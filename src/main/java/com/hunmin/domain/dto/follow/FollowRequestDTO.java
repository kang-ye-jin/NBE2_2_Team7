package com.hunmin.domain.dto.follow;

import com.hunmin.domain.entity.Follow;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FollowRequestDTO {
    private Long followId;
    private Long followerId;
    private Long followeeId;
    private Boolean isBlock;
    private Boolean notification;
    private LocalDateTime createdAt;

    public FollowRequestDTO(Follow follow) {
        this.followId = follow.getFollowId();
        this.followerId = follow.getFollower().getMemberId();
        this.followeeId = follow.getFollowee().getMemberId();
        this.isBlock = follow.getIsBlock();
        this.notification = follow.getNotification();
        this.createdAt = follow.getCreatedAt();
    }
}
