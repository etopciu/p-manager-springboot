package com.ikubinfo.projectmanager.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectStats {
    private Integer id;
    private String title;
    private List<ActiveUsers> activeUsers;
    private long count;
    private long sum;
    private long min;
    private long max;
    private long average;
    private List<TaskStatuses> taskStatuses;

}
