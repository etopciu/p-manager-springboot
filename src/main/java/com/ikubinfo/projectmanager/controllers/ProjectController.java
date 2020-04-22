package com.ikubinfo.projectmanager.controllers;

import com.ikubinfo.projectmanager.models.Project;
import com.ikubinfo.projectmanager.service.ProjectService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/projects")
@Data
public class ProjectController {

    private final ProjectService projectService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<Project>> getProjects () {
        return new ResponseEntity<>(projectService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allPageable")
    public ResponseEntity<Page<Project>> getAllProjects(Pageable pageable) {
        return new ResponseEntity<>(projectService.findAllById(pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Project project) {
        projectService.save(project);
        return new ResponseEntity<>("Project is created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(projectService.findById(id), HttpStatus.OK);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<List<Project>> findByUserId(@PathVariable Long id) {
        return new ResponseEntity<>(projectService.findByUsersId(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public ResponseEntity<Object> update(@Valid @RequestBody Project project) {
       projectService.update(project);
        return new ResponseEntity<>("Project is updated successsfully", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        projectService.deleteById(id);
        return new ResponseEntity<>("Project is deleted successsfully", HttpStatus.OK);
    }

}
