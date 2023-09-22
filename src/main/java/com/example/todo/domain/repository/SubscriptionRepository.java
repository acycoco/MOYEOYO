package com.example.todo.domain.repository;

import com.example.todo.domain.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Page<Subscription> findAllByStatusIsTrue(Pageable pageable);
}
