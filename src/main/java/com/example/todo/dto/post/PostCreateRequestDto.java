package com.example.todo.dto.post;

import com.example.todo.domain.entity.Post;
import com.example.todo.domain.entity.Team;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostCreateRequestDto {

    private String title;

    private String content;

    private List<MultipartFile> images;

    public PostCreateRequestDto(final String title, final String content, final List<MultipartFile> images) {
        this.title = title;
        this.content = content;
        this.images = images;
    }

    public Post toEntity(User user, Team team){
        return Post.builder()
                .title(title)
                .content(content)
                .viewCount(0L)
                .team(team)
                .user(user)
                .build();
    }
}
