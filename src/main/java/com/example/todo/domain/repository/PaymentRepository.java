package com.example.todo.domain.repository;

import com.example.todo.domain.entity.Payment;
import com.example.todo.domain.entity.UsersSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByUsersSubscription(UsersSubscription usersSubscription);
}
