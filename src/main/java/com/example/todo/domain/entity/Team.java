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
public class Team extends BaseTime {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Team(final Long id, final String name, final String description, final String joinCode, final Integer participantNum, final Integer participantNumMax, final User manager, final List<Member> members, final List<Post> posts) {
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
    public List<String> getMemebersNamesList(List<Member> members) {
        List<String> membersNamesList = new ArrayList<>();
        for (Member member : members) membersNamesList.add(member.getUser().getUsername());

        return membersNamesList;
    }
}
