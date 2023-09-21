package com.example.todo.domain.entity.post;

import com.example.todo.domain.entity.BaseTimeEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.image.Image;
import com.example.todo.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE Post SET delete_at = current_timestamp where post_id = ?")
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

    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    private int viewCount;

    @Builder
    public Post(final String title, final String content, final TeamEntity team, final User user, final int viewCount) {
        this.title = title;
        this.content = content;
        this.team = team;
        this.user = user;
        this.viewCount = 0;
    }

    public void addViewCount() {
        this.viewCount++;
    }
}
