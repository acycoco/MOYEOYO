package com.example.todo.domain.repository.user;

import com.example.todo.domain.entity.user.User;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.example.todo.exception.ErrorCode.NOT_FOUND_USER;

public interface UserRepository extends JpaRepository<User, Long> {

    default User getById(Long id) {
        return findById(id).orElseThrow(() -> new TodoAppException(NOT_FOUND_USER, NOT_FOUND_USER.getMessage()));
    }
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
