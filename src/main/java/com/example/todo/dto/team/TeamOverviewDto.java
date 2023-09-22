package com.example.todo.dto.team;

import com.example.todo.domain.entity.Team;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeamOverviewDto {
    private Long id;
    private String teamName;
    private String teamManagerName;
    private String teamDesc;

    public static TeamOverviewDto fromEntity(Team team) {
        TeamOverviewDto teamOverviewDto = new TeamOverviewDto();
        teamOverviewDto.setId(team.getId());
        teamOverviewDto.setTeamName(team.getName());
        teamOverviewDto.setTeamManagerName(team.getManager().getUsername());
        teamOverviewDto.setTeamDesc(team.getDescription());
        return teamOverviewDto;
    }
}
