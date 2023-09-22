package com.example.todo.domain.repository;

import com.example.todo.domain.entity.Post;
import com.example.todo.domain.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByTeam(Team team, Pageable pageable);
}
