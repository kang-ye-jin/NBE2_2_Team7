package com.hunmin.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowException {

    DUPLICATED_FOLLOW("팔로우가 이미 존재합니다.", 400),
    IMPOSSIBLE_FOLLOW("팔로우 할 수 없습니다.", 400),
    NOT_FOUND("팔로우가 존재하지 않습니다.", 400);

    private FollowTaskException followTaskException;

    FollowException(String message, int code){
        followTaskException = new FollowTaskException(message, code);
    }

    public FollowTaskException get(){
        return followTaskException;
    }
}