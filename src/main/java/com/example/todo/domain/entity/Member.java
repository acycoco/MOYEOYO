package com.example.todo.domain.entity;

import com.example.todo.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    private Team team;
    @ManyToOne(cascade=CascadeType.ALL)
    private User user;
}
