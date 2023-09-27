package com.example.todo.domain.repository.post;

import com.example.todo.domain.entity.post.Post;
import com.example.todo.exception.TodoAppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.example.todo.exception.ErrorCode.NOT_FOUND_POST;

public interface PostRepository extends JpaRepository<Post, Long> {

    default Post getById(Long id) {
        return findById(id).orElseThrow(
                () -> new TodoAppException(NOT_FOUND_POST, NOT_FOUND_POST.getMessage())
        );
    }

    Page<Post> findAllByUserIdAndTeamId(Long userId, Long teamId, Pageable pageable);
}
