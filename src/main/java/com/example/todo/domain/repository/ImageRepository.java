package com.example.todo.domain.repository;

import com.example.todo.domain.entity.Image;
import com.example.todo.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteAllByPost_Id(Long postId);
    void deleteAllByPost_IdAndDeletedAtNull(Long postId);
}
