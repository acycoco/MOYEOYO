package com.example.todo.dto.like.response;

import com.example.todo.domain.entity.like.PostLike;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeResponseDto {
    private Long id;
    private String username;
    private Long postId;

    public PostLikeResponseDto(final PostLike postLike) {
        this.id = postLike.getId();
        this.username = postLike.getUser().getUsername();
        this.postId = postLike.getPost().getId();
    }
}
