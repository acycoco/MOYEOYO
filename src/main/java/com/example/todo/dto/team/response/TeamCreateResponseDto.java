package com.example.todo.dto.team.response;

import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamCreateResponseDto {

    private Long id;
    private String name;
    private String description;
    private String joinCode;
    private Integer participantNum
    private Integer participantNumMax;
    private String managerName;

    public TeamCreateResponseDto(final TeamEntity team) {
        this.id = team.getId();
        this.name = team.getName();
        this.description = team.getDescription();
        this.joinCode = team.getJoinCode();
        this.participantNum = team.getParticipantNum();
        this.participantNumMax = team.getParticipantNumMax();
        this.managerName = team.getManager().getUsername();
    }

}
