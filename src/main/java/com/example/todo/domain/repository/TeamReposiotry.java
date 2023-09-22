package com.example.todo.domain.repository;

import com.example.todo.domain.entity.Team;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamReposiotry extends JpaRepository<Team, Long> {
    Page<Team> findAllByNameContainingAndDeletedAtIsNull(String keyword, Pageable pageable);
//    Page<Team> findTeamEntitiesByNameAndDeletedAtEmpty(String keyword, Pageable pageable);
    List<Team> findByMembersUserId(Long userId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t " +
            "from Team t " +
            "where t.id =:teamId")
    Optional<Team> findByIdWithPessimisticLock(@Param("teamId") Long teamId);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select t " +
            "from Team t " +
            "where t.id =:teamId")
    Optional<Team> findByIdWithOptimisticLock(@Param("teamId") Long teamId);
}
