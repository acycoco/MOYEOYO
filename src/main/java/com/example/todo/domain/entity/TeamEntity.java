package com.example.todo.domain.entity;

import com.example.todo.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE todo.team SET deleted_at = current_timestamp where id = ?")
@Where(clause = "deleted_at is null")
@Getter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User manager;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<MemberEntity> members = new ArrayList<>();

    public boolean validateJoinCode(String joinCode){
        return this.getJoinCode().equals(joinCode);
    }

    public Long getManagerId() {
        return manager.getId();
    }
    public List<String> getMemebersNamesList(List<MemberEntity> members) {
        List<String> membersNamesList = new ArrayList<>();
        for (MemberEntity member : members) membersNamesList.add(member.getUser().getUsername());

        return membersNamesList;
    }

    @Builder
    public TeamEntity(final String name, final String description, final String joinCode, final Integer participantNum, final Integer participantNumMax, final User manager) {
        this.name = name;
        this.description = description;
        this.joinCode = joinCode;
        this.participantNum = participantNum;
        this.participantNumMax = participantNumMax;
        this.manager = manager;
    }
}
