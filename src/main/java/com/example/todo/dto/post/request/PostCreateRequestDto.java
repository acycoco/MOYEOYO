package com.example.todo.dto.post.request;

import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public PostCreateRequestDto(final String title, final String content, final List<MultipartFile> images) {
        this.title = title;
        this.content = content;
        this.images = images;
    }

    public Post toEntity(User user, TeamEntity team) {
        return Post.builder()
                .title(title)
                .content(content)
                .team(team)
                .user(user)
                .build();
    }
}
