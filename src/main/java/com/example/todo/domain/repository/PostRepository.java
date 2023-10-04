package com.example.todo.domain.repository;

import com.example.todo.domain.entity.Post;
import com.example.todo.domain.entity.Team;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    default Post getById(Long postId){
        return findById(postId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_POST));
    }
    Page<Post> findAllByTeam(Team team, Pageable pageable);
}
