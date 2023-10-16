package com.example.todo.dto.comment.response;

import com.example.todo.domain.entity.comment.Comment;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentDeleteResponseDto {

    private Long id;
    private String username;
    private LocalDateTime deleteAt;

    public CommentDeleteResponseDto(final Comment comment, final User user) {
        this.id = comment.getId();
        this.username = user.getUsername();
        this.deleteAt = comment.getDeletedAt();
    }
}
