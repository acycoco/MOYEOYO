package com.example.todo.domain.repository;

import com.example.todo.domain.entity.MemberEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.user.User;
import com.example.todo.exception.TodoAppException;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.todo.exception.ErrorCode.NOT_FOUND_MEMBER;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    default void existsByTeamAndUserOrThrow(TeamEntity team, User user){
        if (!existsByTeamAndUser(team, user))
            throw new TodoAppException(NOT_FOUND_MEMBER, NOT_FOUND_MEMBER.getMessage());
    }

    Boolean existsByTeamAndUser(TeamEntity team, User user);
    List<MemberEntity> findAllByUserId(Long userId);
    Optional<MemberEntity> findByTeamAndUser(TeamEntity team, User user);
    List<MemberEntity> findAllByTeamId(Long teamId);
}
