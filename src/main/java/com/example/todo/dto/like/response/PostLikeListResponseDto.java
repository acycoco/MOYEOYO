package com.example.todo.dto.like.response;

import com.example.todo.domain.entity.like.PostLike;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeListResponseDto {
    private Long id;
    private String username;

    public PostLikeListResponseDto(final PostLike postLike) {
        this.id = postLike.getId();
        this.username = postLike.getUser().getUsername();
    }
}
