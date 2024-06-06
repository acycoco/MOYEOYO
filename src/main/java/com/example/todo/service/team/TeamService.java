package com.example.todo.service.team;

import com.example.todo.domain.entity.MemberEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.UsersSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.UsersSubscriptionRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.task.TaskApiDto;
import com.example.todo.dto.team.*;
import com.example.todo.dto.team.request.TeamCreateDto;
import com.example.todo.dto.team.response.TeamCreateResponseDto;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import com.example.todo.service.task.TaskApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {
    private final TeamReposiotry teamReposiotry;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final TaskApiService taskApiService;
    private final UsersSubscriptionRepository usersSubscriptionRepository;
    public static final int FREE_TEAM_PARTICIPANT_NUM = 5;

    @Transactional
    public TeamCreateResponseDto createTeam(Long userId, TeamCreateDto teamCreateDto) {
        User manager = userRepository.getById(userId);

        //팀 최대인원이 5명을 초과할 시 구독권을 구독해야 한다.
        if (teamCreateDto.getParticipantNumMax() > FREE_TEAM_PARTICIPANT_NUM) {
            UsersSubscriptionEntity usersSubscription = usersSubscriptionRepository.findByUsersAndSubscriptionStatus(manager, SubscriptionStatus.ACTIVE)
                    .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_ACTIVE_SUBSCRIPTION));
            if (teamCreateDto.getParticipantNumMax() > usersSubscription.getSubscription().getMaxMember())
                throw new TodoAppException(ErrorCode.EXCEED_ALLOWED_TEAM_MEMBERS);
        }

        TeamEntity team = teamCreateDto.toEntity(manager);

        // manager를 멤버로 추가
        MemberEntity member = MemberEntity.builder()
                .team(team)
                .user(manager)
                .build();


//        teamEntity.setMembers(new ArrayList<>());
//        teamEntity.getMembers().add(member);
//        teamEntity.setParticipantNum(teamEntity.getMembers().size());
        team = teamReposiotry.save(team);
        memberRepository.save(member);
        return new TeamCreateResponseDto(team);
    }

    @Transactional
    public void joinTeam(Long userId, Long teamId, TeamJoinDto teamJoinDto) {
        User user = userRepository.getById(userId);
        TeamEntity team = teamReposiotry.findByIdWithOptimisticLock(teamId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        if (!team.validateJoinCode(teamJoinDto.getJoinCode()))
            throw new TodoAppException(ErrorCode.INVALID_JOIN_CODE, ErrorCode.INVALID_JOIN_CODE.getMessage());

        //팀 멤버수 제한
        if (team.getParticipantNum().equals(team.getParticipantNumMax()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "팀의 최대 허용 멤버 수를 초과했습니다.");


        if (memberRepository.findByTeamAndUser(team, user).isPresent())
            throw new TodoAppException(ErrorCode.ALREADY_USER_JOINED);

        MemberEntity member = new MemberEntity();
        member.setTeam(team);
        member.setUser(user);
        memberRepository.save(member);

        team.getMembers().add(member);
        team.setParticipantNum(team.getParticipantNum() + 1);
        teamReposiotry.save(team);

    }

    public void updateTeamDetails(Long userId, TeamUpdateDto teamUpdateDto, Long teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));
        TeamEntity team = teamReposiotry.findById(teamId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        if (team.getManagerId() != user.getId()) throw new TodoAppException(ErrorCode.MISMATCH_MANAGERID_USERID);


        if (!teamUpdateDto.getName().equals(""))
            team.setName(teamUpdateDto.getName());


        if (!teamUpdateDto.getDescription().equals(""))
            team.setDescription(teamUpdateDto.getDescription());


        if (!teamUpdateDto.getJoinCode().equals(""))
            team.setJoinCode(teamUpdateDto.getJoinCode());


        if (!teamUpdateDto.getManager().getUsername().equals("")) {
            MemberEntity member = memberRepository.findByTeamAndUser(team, user).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_MEMBER));
            team.setManager(member.getUser());
        }

        teamReposiotry.save(team);
    }

    public void deleteTeam(Long userId, Long teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));
        TeamEntity team = teamReposiotry.findById(teamId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        if (team.getManagerId() != user.getId()) throw new TodoAppException(ErrorCode.MISMATCH_MANAGERID_USERID);

        teamReposiotry.delete(team);
    }

    @Transactional
    public void leaveTeam(Long userId, Long teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));
        TeamEntity team = teamReposiotry.findById(teamId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));
        MemberEntity member = memberRepository.findByTeamAndUser(team, user).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_MEMBER));

        member.setTeam(null);
        memberRepository.delete(member);
        team.getMembers().remove(member);
        log.info("part {}", team.getParticipantNum() - 1);
        team.setParticipantNum(team.getParticipantNum() - 1);
        teamReposiotry.save(team);
    }


    public Page<TeamOverviewDto> searchTeam(String keyword, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<TeamEntity> teamEntityPage = teamReposiotry.findAllByNameContainingAndDeletedAtIsNull(keyword, pageable);

        Page<TeamOverviewDto> teamOverviewDtoPage = teamEntityPage.map(TeamOverviewDto::fromEntity);
        return teamOverviewDtoPage;
    }

    public TeamDetailsDto getTeamDetails(Long userId, Long teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));
        TeamEntity team = teamReposiotry.findById(teamId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));
        MemberEntity member = memberRepository.findByTeamAndUser(team, user).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_MEMBER));

        TeamDetailsDto teamDetailsDto = TeamDetailsDto.fromEntity(team);

        List<TaskApiDto> allTasksDtoList = taskApiService.readTasksAll(userId, teamId);
        for (TaskApiDto taskApiDto : allTasksDtoList) {
            teamDetailsDto.getAllTasks().add(taskApiDto);
            if (taskApiDto.getStatus().equals("DONE")) teamDetailsDto.getDoneTasks().add(taskApiDto);
            else teamDetailsDto.getNotDoneTasks().add(taskApiDto);
        }

        return teamDetailsDto;
    }
}
