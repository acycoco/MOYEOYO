package com.example.todo.domain.entity.comment;

import com.example.todo.domain.entity.BaseTimeEntity;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.entity.user.User;
import com.example.todo.dto.comment.request.CommentCreateRequestDto;
import com.example.todo.dto.comment.response.CommentCreateResponseDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE comment SET deleted_at = current_timestamp where id = ?")
@Where(clause = "deleted_at is null")
@Entity
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Comment(final String content, final Post post, final User user) {
        this.content = content;
        this.post = post;
        this.user = user;
    }

    public void update(final CommentCreateRequestDto updateDto) {
        this.content = updateDto.getContent();
    }
    public boolean validatePost(Post post){
        return this.post.equals(post);
    }

    public boolean validateUser(User user){
        return this.user.equals(user);
    }
}
