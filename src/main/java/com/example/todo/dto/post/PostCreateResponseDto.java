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
public class PostCreateResponseDto {

    private Long id;

    private String title;

    private String content;

//    private List<MultipartFile> images;

    public PostCreateResponseDto(final Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
//        this.images = images;
    }

}
