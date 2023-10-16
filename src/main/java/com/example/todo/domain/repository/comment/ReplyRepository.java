package com.example.todo.domain.repository.comment;

import com.example.todo.domain.entity.comment.Comment;
import com.example.todo.domain.entity.comment.Reply;
import com.example.todo.exception.TodoAppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.example.todo.exception.ErrorCode.NOT_FOUND_COMMENT;
import static com.example.todo.exception.ErrorCode.NOT_FOUND_REPLY;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    default Reply getById(Long id) {
        return findById(id).orElseThrow(
                () -> new TodoAppException(NOT_FOUND_REPLY, NOT_FOUND_REPLY.getMessage())
        );
    }
    Page<Reply> findAllByComment(Comment comment, Pageable pageable);
}
