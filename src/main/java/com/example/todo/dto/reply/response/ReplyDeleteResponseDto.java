package com.example.todo.dto.reply.response;

import com.example.todo.domain.entity.comment.Reply;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplyDeleteResponseDto {

    private Long id;
    private Long commentId;
    private String username;
    private LocalDateTime deleteAt;

    public ReplyDeleteResponseDto(final Reply reply, final User user) {
        this.id = reply.getId();
        this.commentId = reply.getComment().getId();
        this.username = user.getUsername();
        this.deleteAt = reply.getDeletedAt();
    }
}
