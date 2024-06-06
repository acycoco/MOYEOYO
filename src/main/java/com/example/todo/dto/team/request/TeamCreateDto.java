package com.example.todo.dto.team.request;

import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCreateDto {

    private String name;
    private String description;
    private String joinCode;
    private Integer participantNumMax;

    @Builder
    public TeamCreateDto(final String name, final String description, final String joinCode, final Integer participantNumMax) {
        this.name = name;
        this.description = description;
        this.joinCode = joinCode;
        this.participantNumMax = participantNumMax;
    }

    public TeamEntity toEntity(User manager){
        return TeamEntity.builder()
                .name(name)
                .description(description)
                .joinCode(joinCode)
                .participantNumMax(participantNumMax)
                .participantNum(1)
                .manager(manager)
                .build();
    }
}
