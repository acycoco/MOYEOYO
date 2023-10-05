package com.example.todo.domain.repository;

import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
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

import static com.example.todo.exception.ErrorCode.NOT_FOUND_TEAM;

@Repository
public interface TeamReposiotry extends JpaRepository<TeamEntity, Long> {

    default TeamEntity getById(Long id) {
        return findById(id).orElseThrow(() -> new TodoAppException(NOT_FOUND_TEAM, NOT_FOUND_TEAM.getMessage()));
    }



    Page<TeamEntity> findAllByNameContainingAndDeletedAtIsNull(String keyword, Pageable pageable);
//    Page<TeamEntity> findTeamEntitiesByNameAndDeletedAtEmpty(String keyword, Pageable pageable);
    List<TeamEntity> findByMembersUserId(Long userId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t " +
            "from TeamEntity t " +
            "where t.id =:teamId")
    Optional<TeamEntity> findByIdWithPessimisticLock(@Param("teamId") Long teamId);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select t " +
            "from TeamEntity t " +
            "where t.id =:teamId")
    Optional<TeamEntity> findByIdWithOptimisticLock(@Param("teamId") Long teamId);
}
