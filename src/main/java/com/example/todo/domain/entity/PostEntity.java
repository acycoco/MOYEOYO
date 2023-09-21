package com.example.todo.domain.entity;

import com.example.todo.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String fileUrl;

    private Long viewCount;

    @ManyToOne
    private TeamEntity team;

    @ManyToOne
    private User writer;

    @Builder
    public PostEntity(final Long id, final String title, final String content, final String fileUrl, final Long viewCount, final TeamEntity team, final User writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.fileUrl = fileUrl;
        this.viewCount = viewCount;
        this.team = team;
        this.writer = writer;
    }
}
