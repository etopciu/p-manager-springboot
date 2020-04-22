package com.ikubinfo.projectmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private Date createdDate;
    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ETask status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "task_users",
//            joinColumns = @JoinColumn(name = "task_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private Set<User> users = new HashSet<>();

}
