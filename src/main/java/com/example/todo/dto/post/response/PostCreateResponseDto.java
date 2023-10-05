package com.example.todo.dto.post.response;

import com.example.todo.domain.entity.post.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostCreateResponseDto {

    private Long id;

    private String title;

    private String content;


    public PostCreateResponseDto(final Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
