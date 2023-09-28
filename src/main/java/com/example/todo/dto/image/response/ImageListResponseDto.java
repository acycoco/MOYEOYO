package com.example.todo.dto.image.response;

import com.example.todo.domain.entity.image.Image;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ImageListResponseDto {

    private Long id;
    private String imageUrl;

    public ImageListResponseDto(final Image image) {
        this.id = image.getId();
        this.imageUrl = image.getImage();
    }
}
