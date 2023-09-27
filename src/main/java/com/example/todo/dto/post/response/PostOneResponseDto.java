package com.example.todo.dto.post.response;

import com.example.todo.domain.entity.post.Post;
import com.example.todo.dto.image.response.ImageListResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostOneResponseDto {

    private Long id;
    private String username;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    private long likeCount;
    private int viewCount;
    private List<ImageListResponseDto> imageUrl;

    public PostOneResponseDto(final Post post) {
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.title = post.getTitle();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
//        this.likeCount = likeCount;
        this.viewCount = post.getViewCount();
        this.imageUrl = post.getImages().stream()
                .map(ImageListResponseDto::new)
                .collect(Collectors.toList());
    }
}
