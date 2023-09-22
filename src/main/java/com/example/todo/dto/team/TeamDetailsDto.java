package com.example.todo.dto.team;

import com.example.todo.domain.entity.Team;
//import com.example.todo.dto.task.TaskApiDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamDetailsDto {
    private String name;
    private String managerName;
    private String desc;
    private List<String> members;
    private Integer memberLimit;
//    private List<TaskApiDto> notDoneTasks;
//    private List<TaskApiDto> doneTasks;
//    private List<TaskApiDto> allTasks;

    public static TeamDetailsDto fromEntity(Team team) {
        TeamDetailsDto teamDetailsDto = new TeamDetailsDto();
        teamDetailsDto.setName(team.getName());
        teamDetailsDto.setManagerName(team.getManager().getUsername());
        teamDetailsDto.setDesc(team.getDescription());
        teamDetailsDto.setMembers(team.getMemebersNamesList(team.getMembers()));
        teamDetailsDto.setMemberLimit(team.getParticipantNumMax());

        return teamDetailsDto;
    }
}
