package com.ikubinfo.projectmanager.repository;

import com.ikubinfo.projectmanager.models.Project;
import com.ikubinfo.projectmanager.payload.response.ActiveUsers;
import com.ikubinfo.projectmanager.payload.response.TaskStatuses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {

    @Override
    Page<Project> findAll ( Pageable pageable);
    boolean existsByTitle(String title);
    List<Project> findByUsers_Id(Long Id);

    @Query(value = "SELECT new com.ikubinfo.projectmanager.payload.response.TaskStatuses(ts.status, count(ts.status)) from Task ts where ts.project.id = :id group by ts.status")
    List<TaskStatuses>  findByTaskStatuses (@PathVariable  Integer id);

    @Query(value = "SELECT new com.ikubinfo.projectmanager.payload.response.ActiveUsers (ts.user.username, count(ts.user.id)) from Task ts where ts.project.id = :id group by ts.user.id")
    List<ActiveUsers>  findByProjectUsers (@PathVariable  Integer id);

}
