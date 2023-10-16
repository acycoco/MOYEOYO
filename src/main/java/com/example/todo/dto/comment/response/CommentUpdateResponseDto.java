package com.example.todo.dto.comment.response;

import com.example.todo.domain.entity.comment.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentUpdateResponseDto {

    private Long id;
    private String content;
    private LocalDateTime updatedAt;

    public CommentUpdateResponseDto(final Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.updatedAt = comment.getUpdatedAt();
    }
}
