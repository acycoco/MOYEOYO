package com.example.todo.dto;

import com.example.todo.domain.entity.NotificationEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private String content;

    public static NotificationDto fromEntity(NotificationEntity entity) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setContent(entity.getContent());
        return notificationDto;
    }
}