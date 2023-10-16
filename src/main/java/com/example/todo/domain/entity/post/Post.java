package com.example.todo.domain.entity.post;

import com.example.todo.domain.entity.BaseTimeEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.comment.Comment;
import com.example.todo.domain.entity.image.Image;
import com.example.todo.domain.entity.like.PostLike;
import com.example.todo.domain.entity.user.User;
import com.example.todo.dto.post.request.PostUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE todo.post SET deleted_at = current_timestamp where id = ?")
@Where(clause = "deleted_at is null")
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikes = new ArrayList<>();

    private int viewCount;

    private int likeCount;

    @Version
    private Long version;

    @Builder
    public Post(final String title, final String content, final TeamEntity team, final User user, final int viewCount, final int likeCount) {
        this.title = title;
        this.content = content;
        this.team = team;
        this.user = user;
        this.viewCount = 0;
        this.likeCount = 0;
    }

    public void addViewCount() {
        this.viewCount++;
    }

    public void addLikeCount() {
        this.likeCount++;
    }

    public void removeLikeCount() {
        this.likeCount--;
    }

    public void update(final PostUpdateRequestDto updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
    }

    public boolean validateTeam(TeamEntity team){
        return this.team.equals(team);
    }

    public void addPostLike(PostLike postLike){
        this.postLikes.add(postLike);
    }

    public void removePostLike(PostLike postLike){
        this.postLikes.remove(postLike);
    }
}
