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
public class PostResponseDto {

    private String title;

    private String content;

    private Long viewCount;

    private String teamName;

    private String writer;

    private List<String> imageUrls = new ArrayList<>();

    public void changeImageUrls(List<Image> images){
        if (images != null){
            this.imageUrls = images.stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList());
        }
    }

    @Builder
    public PostResponseDto(String title, String content, Long viewCount, String teamName, String writer, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.teamName = teamName;
        this.writer = writer;
        this.imageUrls = imageUrls;
    }


    public static PostResponseDto fromEntity(Post entity) {
        PostResponseDto postResponseDto = PostResponseDto.builder()
                .title(entity.getTitle())
                .content(entity.getContent())
                .viewCount(entity.getViewCount())
                .teamName(entity.getTeam().getName())
                .writer(entity.getWriter().getUsername())
                .build();

        postResponseDto.changeImageUrls(entity.getImages());
        return postResponseDto;
    }
}
