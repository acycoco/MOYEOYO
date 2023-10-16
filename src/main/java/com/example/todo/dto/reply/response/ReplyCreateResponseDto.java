package com.example.todo.dto.reply.response;

import com.example.todo.domain.entity.comment.Comment;
import com.example.todo.domain.entity.comment.Reply;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplyCreateResponseDto {

    private Long id;
    private Long commentId;
    private String content;
    private LocalDateTime createAt;

    public ReplyCreateResponseDto(final Reply reply) {
        this.id = reply.getId();
        this.commentId = reply.getComment().getId();
        this.content = reply.getContent();
        this.createAt = reply.getCreatedAt();
    }
}
