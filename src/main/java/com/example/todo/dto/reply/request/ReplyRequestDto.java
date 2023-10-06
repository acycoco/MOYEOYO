package com.example.todo.dto.reply.request;

import com.example.todo.domain.entity.comment.Comment;
import com.example.todo.domain.entity.comment.Reply;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplyRequestDto {

    private String content;

    public ReplyRequestDto(final String content) {
        this.content = content;
    }

    public Reply toEntity(Comment comment, User user){
        return Reply.builder()
                .comment(comment)
                .user(user)
                .content(this.content)
                .build();
    }
}
