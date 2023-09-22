package com.example.todo.domain.repository;

import com.example.todo.domain.entity.Member;
import com.example.todo.domain.entity.Team;
import com.example.todo.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByUserId(Long userId);
    Optional<Member> findByTeamAndUser(Team team, User user);
    List<Member> findAllByTeamId(Long teamId);
}
