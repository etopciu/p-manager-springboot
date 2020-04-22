package com.ikubinfo.projectmanager.controllers;

import com.ikubinfo.projectmanager.models.User;
import com.ikubinfo.projectmanager.service.UserService;
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
@RequestMapping("/api/users")
@Data
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<User>> getUsers () {
        return  new ResponseEntity(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id),HttpStatus.OK);
    }

    @GetMapping("tasks/{id}")
    public ResponseEntity<User> findTasksById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.findTasksById(id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public ResponseEntity update(@Valid @RequestBody User user) {
        userService.update(user);
        return new ResponseEntity<>("User updated succesfully",HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity ("User deleted succesfully",HttpStatus.OK);
    }

}
