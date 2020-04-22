package com.ikubinfo.projectmanager.service;

import com.ikubinfo.projectmanager.error.EntityAlreadyExistsException;
import com.ikubinfo.projectmanager.models.*;
import com.ikubinfo.projectmanager.payload.request.LoginRequest;
import com.ikubinfo.projectmanager.payload.request.SignupRequest;
import com.ikubinfo.projectmanager.payload.response.JwtResponse;
import com.ikubinfo.projectmanager.repository.ProjectRepository;
import com.ikubinfo.projectmanager.repository.RoleRepository;
import com.ikubinfo.projectmanager.repository.TaskRepository;
import com.ikubinfo.projectmanager.repository.UserRepository;
import com.ikubinfo.projectmanager.security.jwt.JwtUtils;
import com.ikubinfo.projectmanager.security.services.UserDetailsImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Data
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;


    public List<User> findAll (){
        return userRepository.findAll();
    }

    public User findById (Long id){
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            log.error("Id " + id + " is not existed");
            throw new EntityNotFoundException("User not found");
        }
        return user.get();
    }

    public void update(User user) {
        Optional<User> user1 = userRepository.findById(user.getId());

        if (!user1.isPresent()) {
            log.error("Id " + user.getId() + " is not existed");
            throw (new EntityNotFoundException("User does not exist"));
        } else if (!user1.get().getUsername().equals(user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
            log.error("User with username "+ user.getUsername() + "already exists");
            throw (new EntityAlreadyExistsException("User with this username already exists"));
        } else if (!user1.get().getEmail().equals(user.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            log.error("User with email " + user.getEmail() + "already exists");
            throw (new EntityAlreadyExistsException("User with this email already exists"));
        } else userRepository.save(user);
    }
    public User save  (User user){
        return userRepository.save(user);
    }

    public void deleteById(Long id){
        if (!userRepository.findById(id).isPresent()) {
            log.error("Id " + id + " is not existed");
            throw (new EntityNotFoundException("User does not exist"));
        } else {
            Optional <User> user = userRepository.findById(id);
            for (Project p : user.get().getProjects() ){
                p.getUsers().remove(user.get());
                projectRepository.save(p);
            }

            userRepository.deleteById(id);
        }

    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    public void registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new EntityAlreadyExistsException("User with Username already exists ! ");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EntityAlreadyExistsException("User with Email already exists ! ");
        }
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        String strRole = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRole == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {

            switch (strRole) {
                case "ADMIN":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                default:
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
    }

    public User findTasksById(Integer id) {
        return userRepository.findByTasks_Id(id);
    }
}
