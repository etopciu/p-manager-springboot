package com.ikubinfo.projectmanager.payload.request;

import lombok.Data;

@Data
public class ProjetAssignTaskRequest {
    private Integer projectId;
    private Integer taskId;
}
