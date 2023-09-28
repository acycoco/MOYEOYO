package com.example.todo.dto.post.response;

import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostListResponseDto {

    private Long id;
    private String title;
    private String username;
    private String imageUrl;
    private int viewCount;

    public PostListResponseDto(final Post post, final User user) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = user.getUsername();
        this.imageUrl = post.getImages().stream()
                .map(image -> image.getImage())
                .findFirst().orElse(null);
        this.viewCount = post.getViewCount();
    }
}
