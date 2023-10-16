package com.example.todo.domain.repository.post;

import com.example.todo.domain.entity.post.Post;
import com.example.todo.exception.TodoAppException;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.example.todo.exception.ErrorCode.NOT_FOUND_POST;

public interface PostRepository extends JpaRepository<Post, Long> {

    default Post getById(Long id) {
        return findById(id).orElseThrow(
                () -> new TodoAppException(NOT_FOUND_POST, NOT_FOUND_POST.getMessage())
        );
    }

    Page<Post> findAllByUserIdAndTeamId(Long userId, Long teamId, Pageable pageable);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select p " +
            "from Post p " +
            "where p.id =:postId")
    Optional<Post> findByIdWithOptimisticLock(@Param("postId") Long postId);
}
