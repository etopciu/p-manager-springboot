package com.ikubinfo.projectmanager.controllers;

import com.ikubinfo.projectmanager.models.Task;
import com.ikubinfo.projectmanager.payload.response.TaskUser;
import com.ikubinfo.projectmanager.service.TaskService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/tasks")
@Data
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<List<Task>> getTasks () {
        return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{projectId}/{userId}")
    public ResponseEntity create(@PathVariable Integer projectId,@PathVariable Long userId,@Valid @RequestBody Task task) {
        taskService.save(task,projectId,userId);
        return new ResponseEntity<>("Task is created successfully", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/start/{id}")
    public ResponseEntity start(@PathVariable Integer id) {
        taskService.start(id);
        return new ResponseEntity<>("Task is updated successfully", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/finish/{id}")
    public ResponseEntity finish(@PathVariable Integer id) {
        taskService.finish(id);
        return new ResponseEntity<>("Task is updated successfully", HttpStatus.OK);
    }

    @GetMapping("project/{id}")
    public ResponseEntity<List <TaskUser>> findByProjectId(@PathVariable Integer id) {
        return new ResponseEntity<>(taskService.findByProjectId(id), HttpStatus.OK);
    }

    @GetMapping("user/{userId}/project/{projectId}")
    public ResponseEntity<List <Task>> findByUserIdAndProjectId(@PathVariable Long userId,@PathVariable Integer projectId) {
        return new ResponseEntity<>(taskService.findByUserIdAndProjectId(userId,projectId), HttpStatus.OK);
    }

    @GetMapping("project/{projectId}/task/{taskId}")
    public ResponseEntity<Task> findByIdAndProjectId(@PathVariable Integer projectId, @PathVariable Integer taskId) {
        return new ResponseEntity<>(taskService.findByIdAndProjectId(projectId,taskId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{projectId}/{userId}")
    public ResponseEntity update(@PathVariable Integer projectId,@PathVariable Long userId, @Valid @RequestBody Task task) {
        taskService.update(task,projectId,userId);
        return new ResponseEntity<>("Task is updated successsfully", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        taskService.deleteById(id);
        return new ResponseEntity<>("Task is deleted successsfully", HttpStatus.OK);
    }

}
