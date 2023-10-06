package com.example.todo.dto.comment.response;

import com.example.todo.domain.entity.comment.Comment;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentListResponseDto {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentListResponseDto(final Comment comment, final User user) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = user.getUsername();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
