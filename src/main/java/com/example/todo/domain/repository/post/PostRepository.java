package com.example.todo.domain.repository.post;

import com.example.todo.domain.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByUserIdAndTeamId(Long userId, Long teamId, Pageable pageable);
}
