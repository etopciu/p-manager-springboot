package com.ikubinfo.projectmanager.controllers;

import com.ikubinfo.projectmanager.payload.response.ProjectStats;
import com.ikubinfo.projectmanager.payload.response.Stats;
import com.ikubinfo.projectmanager.repository.ProjectRepository;
import com.ikubinfo.projectmanager.repository.UserRepository;
import com.ikubinfo.projectmanager.service.StatService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/stats")
@Data
public class StatsController {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final StatService statService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<Stats> getStats(){
        return new ResponseEntity<>(statService.getStats(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/project/{id}")
    public ResponseEntity<ProjectStats> getProjectStats(@PathVariable Integer id){
        return new ResponseEntity<>(statService.getProjectStats(id), HttpStatus.OK);

    }
}
