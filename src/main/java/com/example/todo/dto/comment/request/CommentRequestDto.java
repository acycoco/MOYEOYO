package com.example.todo.dto.comment.request;

import com.example.todo.domain.entity.comment.Comment;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentRequestDto {

    private String content;

    public CommentRequestDto(final String content) {
        this.content = content;
    }

    public Comment toEntity(Post post, User user){
        return Comment.builder()
                .post(post)
                .user(user)
                .content(this.content)
                .build();
    }
}
