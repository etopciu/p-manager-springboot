package com.ikubinfo.projectmanager.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Stats {
    private Long projects;
    private Long tasks;
    private List <TaskStatuses> taskStatuses;
    private List<ActiveUsers> activeUsers;

}
