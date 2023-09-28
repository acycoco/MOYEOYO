package com.example.todo.dto.post.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostUpdateRequestDto {

    private String title;
    private String content;

    @Builder
    public PostUpdateRequestDto(final String title, final String content) {
        this.title = title;
        this.content = content;
    }
}
