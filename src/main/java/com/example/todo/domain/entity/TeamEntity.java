package com.example.todo.domain.entity;

import com.example.todo.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String joinCode;
    private Integer participantNum;
    private Integer participantNumMax;
//
//    @Version
//    private Long version;

    @ManyToOne
    private User manager;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    private List<MemberEntity> members = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Post> posts = new ArrayList<>();

    @Builder
    public TeamEntity(final Long id, final String name, final String description, final String joinCode, final Integer participantNum, final Integer participantNumMax, final User manager, final List<MemberEntity> members, final List<Post> posts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.joinCode = joinCode;
        this.participantNum = participantNum;
        this.participantNumMax = participantNumMax;
        this.manager = manager;
        this.members = members;
        this.posts = posts;
    }


    public Boolean containsMember(User user) {
        return this.members.stream()
                .anyMatch(member -> member.getUser().equals(user));
    }



    public Long getManagerId() {
        return manager.getId();
    }
    public List<String> getMemebersNamesList(List<MemberEntity> members) {
        List<String> membersNamesList = new ArrayList<>();
        for (MemberEntity member : members) membersNamesList.add(member.getUser().getUsername());

        return membersNamesList;
    }
}
