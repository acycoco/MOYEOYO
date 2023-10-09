package com.example.todo.domain.repository.like;

import com.example.todo.domain.entity.like.PostLike;
import com.example.todo.domain.entity.post.Post;
import com.example.todo.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, User user);
    boolean existsByPostAndUser(Post post, User user);
    Page<PostLike> findAllByPost(Post post, Pageable pageable);

}
