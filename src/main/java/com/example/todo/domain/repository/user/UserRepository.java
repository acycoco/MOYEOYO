package com.example.todo.domain.repository.user;

import com.example.todo.domain.entity.user.User;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    default User getById(Long userId){
        return findById(userId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));

    }
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
