package com.ikubinfo.projectmanager.service;

import com.ikubinfo.projectmanager.models.ETask;
import com.ikubinfo.projectmanager.models.Project;
import com.ikubinfo.projectmanager.models.Task;
import com.ikubinfo.projectmanager.payload.response.ActiveUsers;
import com.ikubinfo.projectmanager.payload.response.ProjectStats;
import com.ikubinfo.projectmanager.payload.response.Stats;
import com.ikubinfo.projectmanager.payload.response.TaskStatuses;
import com.ikubinfo.projectmanager.repository.ProjectRepository;
import com.ikubinfo.projectmanager.repository.TaskRepository;
import com.ikubinfo.projectmanager.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@Slf4j
public class StatService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ProjectStats getProjectStats(Integer id){
        Optional<Project> project = projectRepository.findById(id);
        List <Task> tasks = taskRepository.findByProjectId(id);
        List <TaskStatuses> taskStatuses = projectRepository.findByTaskStatuses(id);
        List<ActiveUsers> activeUsers = projectRepository.findByProjectUsers(id);
        LongSummaryStatistics stats = tasks.stream()
                .filter(task -> task.getStatus()== ETask.COMPLETED)
                .collect(Collectors.toList())
                .stream()
                .map(ft -> (ft.getEndDate().getTime()-ft.getStartDate().getTime()))
                .collect(Collectors.toList())
                .stream().mapToLong((x)-> x).summaryStatistics();
        ProjectStats projectStats = new ProjectStats(project.get().getId(),project.get().getTitle(),activeUsers,stats.getCount(),stats.getSum()/60000,stats.getMin()/60000,stats.getMax()/60000,(long)stats.getAverage()/60000,taskStatuses);
        return projectStats;
    }

    public Stats getStats() {
        List<TaskStatuses> taskStatuses = taskRepository.findByTaskStatuses();
        List<ActiveUsers> activeUsers = userRepository.findMostActiveUsers();
        Long totalProjects = projectRepository.count();
        Long totalTasks = taskRepository.count();

        Stats stats = new Stats(totalProjects,totalTasks,taskStatuses,activeUsers);
        return stats;
    }
}
