package com.example.todo.dto.reply.response;

import com.example.todo.domain.entity.comment.Reply;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplyOneResponseDto {

    private Long id;
    private Long commentId;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReplyOneResponseDto(final Reply reply, final User user) {
        this.id = reply.getId();
        this.commentId = reply.getComment().getId();
        this.content = reply.getContent();
        this.username = user.getUsername();
        this.createdAt = reply.getCreatedAt();
        this.updatedAt = reply.getUpdatedAt();
    }
}
