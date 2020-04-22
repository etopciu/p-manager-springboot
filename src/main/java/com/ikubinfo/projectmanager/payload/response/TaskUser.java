package com.ikubinfo.projectmanager.payload.response;

import com.ikubinfo.projectmanager.models.ETask;
import com.ikubinfo.projectmanager.models.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUser {
    private Integer id;
    private String title;
    private String description;
    private Date createdDate;
    private Date startDate;
    private Date endDate;;
    private ETask status;
    private String username;
}
