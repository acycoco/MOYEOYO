package com.example.todo.domain.entity;

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

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Post extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private Long viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    public void increaseViewCount() {
        this.viewCount++;
    }
    public Boolean isWriter(User user){
        return this.user.equals(user);
    }

    public Boolean isTeam(Team team){
        return this.team.equals(team);
    }

    public void addImage(Image image){
        this.images.add(image);
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }

    @Builder
    public Post(Long id, String title, String content, Long viewCount, Team team, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.team = team;
        this.user = user;
    }
}
