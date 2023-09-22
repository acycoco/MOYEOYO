package com.example.todo.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE image SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Image extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Builder
    public Image(Long id, String filename, String imageUrl, Post post) {
        this.id = id;
        this.filename = filename;
        this.imageUrl = imageUrl;
        this.post = post;
    }
}
