package com.example.todo.dto.image.request;

import com.example.todo.domain.entity.image.Image;
import com.example.todo.domain.entity.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ImageCreateRequestDto {

    public static Image toEntity(final Post post, final String imageUrl) {
        return Image.builder()
                .post(post)
                .image(imageUrl)
                .build();
    }
}
