package com.example.todo.domain.entity.user;

import com.example.todo.domain.entity.PostEntity;
import com.example.todo.domain.entity.UsersSubscriptionEntity;
import com.example.todo.domain.entity.enums.Role;
import com.example.todo.dto.user.request.UserUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String profileImage;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "users")
    private List<UsersSubscriptionEntity> usersSubscriptions;

    @OneToMany(mappedBy = "users")
    private List<PostEntity> posts;

    public User(Long id, String username, String password, String profileImage, String phone, Role role, List<UsersSubscriptionEntity> usersSubscriptions, List<PostEntity> posts) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
        this.phone = phone;
        this.role = role;
        this.usersSubscriptions = usersSubscriptions;
        this.posts = posts;
    }

    @Builder


    public void updateProfile(final UserUpdateRequestDto updateDto, final String password, final String profileImage) {
        this.password = password;
        this.phone = updateDto.getPhone();
        this.profileImage = profileImage;
    }

    public User update(String picture) {
        this.profileImage = picture;
        return this;
    }
}
