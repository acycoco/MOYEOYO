package com.example.todo.domain.entity.chat;

import com.example.todo.domain.entity.BaseTime;
//import com.example.todo.domain.entity.TaskApiEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
public class ChatRoom extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "task_id")
//    private TaskApiEntity taskApiEntity;

}
