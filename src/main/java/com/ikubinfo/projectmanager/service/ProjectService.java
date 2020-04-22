package com.ikubinfo.projectmanager.service;

import com.ikubinfo.projectmanager.error.EntityAlreadyExistsException;
import com.ikubinfo.projectmanager.models.Project;
import com.ikubinfo.projectmanager.payload.response.MessageResponse;
import com.ikubinfo.projectmanager.repository.ProjectRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Data
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;

    public List<Project> findAll (){
        return projectRepository.findAll();
    }

    public Project findById (Integer id){
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            log.error("Id " + id + " is not existed");
            throw (new EntityNotFoundException("Project does not exist"));
        } else return project.get();

    }

    public Project save (Project project){
        if (projectRepository.existsByTitle(project.getTitle())) {
            log.error("Project with title "+ project.getTitle() + "already exists");
            throw (new EntityAlreadyExistsException("Project with this title already exists"));
        }
        return projectRepository.save(project);
    }

    public void deleteById(Integer id){
        if (!projectRepository.findById(id).isPresent()) {
            log.error("Id " + id + " is not existed");
            throw (new EntityNotFoundException("Project does not exist"));
        }
            Optional<Project> project =  projectRepository.findById(id);
            projectRepository.delete(project.get());
    }


    public void update(Project project) {
        Optional<Project> project1 = projectRepository.findById(project.getId());

        if (!project1.isPresent()) {
            log.error("Id " + project.getId() + " is not existed");
            throw (new EntityNotFoundException("Project does not exist"));
        } else if (!project1.get().getTitle().equals(project.getTitle()) && projectRepository.existsByTitle(project.getTitle())) {
            log.error("Project with title "+ project.getTitle() + "already exists");
            throw (new EntityAlreadyExistsException("Project with this title already exists"));
        }else projectRepository.save(project);
    }

    public Page<Project> findAllById(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    public  List<Project> findByUsersId(Long id) {
        List<Project> project = projectRepository.findByUsers_Id(id);
        if (project.size()<1) {
            log.error("Project with UserId " + id + " does not exist");
            throw (new EntityNotFoundException("Project does not exist"));
        }
        return projectRepository.findByUsers_Id(id);
    }
}
