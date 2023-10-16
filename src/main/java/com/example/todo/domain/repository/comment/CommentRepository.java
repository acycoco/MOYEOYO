package com.example.todo.domain.repository.comment;

import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.comment.Comment;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.exception.TodoAppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.example.todo.exception.ErrorCode.NOT_FOUND_COMMENT;
import static com.example.todo.exception.ErrorCode.NOT_FOUND_POST;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    default Comment getById(Long id) {
        return findById(id).orElseThrow(
                () -> new TodoAppException(NOT_FOUND_COMMENT, NOT_FOUND_COMMENT.getMessage())
        );
    }
    Page<Comment> findAllByPost(Post post, Pageable pageable);
}
