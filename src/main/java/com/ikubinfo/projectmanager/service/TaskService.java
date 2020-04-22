package com.ikubinfo.projectmanager.service;

import com.ikubinfo.projectmanager.error.EntityAlreadyExistsException;
import com.ikubinfo.projectmanager.models.Project;
import com.ikubinfo.projectmanager.models.Task;
import com.ikubinfo.projectmanager.models.User;
import com.ikubinfo.projectmanager.payload.response.TaskUser;
import com.ikubinfo.projectmanager.repository.ProjectRepository;
import com.ikubinfo.projectmanager.repository.TaskRepository;
import com.ikubinfo.projectmanager.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

import static com.ikubinfo.projectmanager.models.ETask.*;

@Service
@Data
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    public List<Task> findAll (){
        return taskRepository.findAll();
    }

    public List<TaskUser> findByProjectId (Integer id){
        List<Task> tasks = taskRepository.findByProjectId(id);
        List<TaskUser> taskUsers = new ArrayList<>();
        for (Task task : tasks){
            User user = userRepository.findByTasks_Id(task.getId());
            TaskUser taskUser = new TaskUser();
            taskUser.setId(task.getId());
            taskUser.setTitle(task.getTitle());
            taskUser.setDescription(task.getDescription());
            taskUser.setCreatedDate(task.getCreatedDate());
            taskUser.setStartDate(task.getStartDate());
            taskUser.setEndDate(task.getEndDate());
            taskUser.setStatus(task.getStatus());
            taskUser.setUsername(user.getUsername());
            taskUsers.add(taskUser);
        }
            return taskUsers;
    }

    public Task findByIdAndProjectId (Integer projectId,Integer taskId){
        Optional <Task> task = taskRepository.findByIdAndProjectId(taskId,projectId);
        if (!task.isPresent()) {
            log.error("Id " + taskId + " is not existed");
            throw (new EntityNotFoundException("Task does not exist"));
        }
        return task.get();
    }

    public Task save (Task task,Integer projectId,Long userId){
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<User> user = userRepository.findById(userId);

        if (!project.isPresent()) {
            log.error("Id " + projectId + " does not exist");
            throw (new EntityNotFoundException("Project does not exist"));
        } else if (!user.isPresent()) {
            log.error("Id " + userId + " does not exist");
            throw (new EntityNotFoundException("User does not exist"));
        } else if (taskRepository.findByProjectIdAndTitleEquals(projectId,task.getTitle()).size()>0) {
            log.error("Task with title "+ task.getTitle() + "already exists");
            throw (new EntityAlreadyExistsException("Task with this title already exists"));
        } else
            task.setProject(project.get());
            task.setCreatedDate(Calendar.getInstance().getTime());
            task.setStatus(PENDING);
            task.setUser(user.get());
         return taskRepository.save(task);

    }

    public void start(Integer id) {
        Optional<Task> task = taskRepository.findById(id);
        if (!task.isPresent()) {
            log.error("Id " + id + " is not existed");
            throw (new EntityNotFoundException("Task does not exist"));
        }
        task.get().setStatus(ONGOING);
        task.get().setStartDate(Calendar.getInstance().getTime());
        taskRepository.save(task.get());
    }

    public void finish (Integer id){
        Optional<Task> task = taskRepository.findById(id);
        if (!task.isPresent()) {
            log.error("Id " + id + " is not existed");
            throw (new EntityNotFoundException("Task does not exist"));
        }
        task.get().setStatus(COMPLETED);
        task.get().setEndDate(Calendar.getInstance().getTime());
        taskRepository.save(task.get());
    }

    public void update(Task task,Integer projectId,Long userId) {
        Optional<Task> task1 = taskRepository.findById(task.getId());
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<User> user = userRepository.findById(userId);

        if (!task1.isPresent()) {
            log.error("Id " + task.getId() + " is not existed");
            throw (new EntityNotFoundException("Task does not exist"));
        } else if (!task1.get().getTitle().equals(task.getTitle()) && taskRepository.findByProjectIdAndTitleEquals(projectId,task.getTitle()).size()>0) {
            log.error("Task with title "+ task.getTitle() + "already exists");
            throw (new EntityAlreadyExistsException("Task with this title already exists"));
        }
        if (!project.isPresent()) {
            log.error("Id " + projectId + " does not exist");
            throw (new EntityNotFoundException("Project does not exist"));
        }
            task.setUser(user.get());
            task.setProject(project.get());
            taskRepository.save(task);
    }

    public void deleteById(Integer id){
        if (!taskRepository.findById(id).isPresent()) {
            log.error("Id " + id + " is not existed");
            throw (new EntityNotFoundException("Task does not exist"));
        } else taskRepository.deleteById(id);
    }

    public List<Task> findByUserIdAndProjectId(Long userId, Integer projectId) {
        List<Task> tasks = taskRepository.findByProjectIdAndUserId(projectId,userId);
        if (tasks.size()<1) {
            log.error("Task with userId " + userId + " and projectId " + projectId + " does not exist");
            throw (new EntityNotFoundException("Task does not exist"));
        }
        return tasks;
    }
}