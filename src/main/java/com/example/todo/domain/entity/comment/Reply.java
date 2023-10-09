package com.example.todo.domain.entity.comment;

import com.example.todo.domain.entity.BaseTimeEntity;
import com.example.todo.domain.entity.user.User;
import com.example.todo.dto.reply.request.ReplyRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE reply SET deleted_at = current_timestamp where id = ?")
@Where(clause = "deleted_at is null")
@Entity
public class Reply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Reply(final String content, final Comment comment, final User user) {
        this.content = content;
        this.comment = comment;
        this.user = user;
    }

    public void update(final ReplyRequestDto updateDto) {
        this.content = updateDto.getContent();
    }
    public boolean validateComment(Comment comment){
        return this.comment.equals(comment);
    }

    public boolean validateUser(User user){
        return this.user.equals(user);
    }
}
