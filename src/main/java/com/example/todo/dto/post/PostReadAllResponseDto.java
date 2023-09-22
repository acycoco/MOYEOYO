package com.example.todo.dto.post;

import com.example.todo.domain.entity.Image;
import com.example.todo.domain.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReadAllResponseDto {

    private String title;

    private String content;

    private Long viewCount;

    private String teamName;

    private String writer;


    @Builder
    public PostReadAllResponseDto(String title, String content, Long viewCount, String teamName, String writer) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.teamName = teamName;
        this.writer = writer;
    }


    public static PostReadAllResponseDto fromEntity(Post entity) {
        PostReadAllResponseDto postReadAllResponseDto = PostReadAllResponseDto.builder()
                .title(entity.getTitle())
                .content(entity.getContent())
                .viewCount(entity.getViewCount())
                .teamName(entity.getTeam().getName())
                .writer(entity.getWriter().getUsername())
                .build();

        return postReadAllResponseDto;
    }
}
