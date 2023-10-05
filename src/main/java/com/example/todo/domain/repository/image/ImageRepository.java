package com.example.todo.domain.repository.image;

import com.example.todo.domain.entity.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
