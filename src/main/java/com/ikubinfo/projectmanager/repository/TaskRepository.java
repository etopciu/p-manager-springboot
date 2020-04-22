package com.ikubinfo.projectmanager.repository;

import com.ikubinfo.projectmanager.models.Task;
import com.ikubinfo.projectmanager.payload.response.TaskStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {
    boolean existsByTitle(String title);
    List<Task> findByProjectIdAndTitleEquals(Integer id,String title);
    List<Task>  findByProjectId(Integer id);
    List<Task>  findByUserId(Long id);
    List<Task>  findByProjectIdAndUserId(Integer projectId,Long userId);
    Optional<Task> findByIdAndProjectId(Integer taskId, Integer projectId);
    @Query(value = "SELECT new com.ikubinfo.projectmanager.payload.response.TaskStatuses(ts.status, count(ts.status)) from Task ts group by ts.status")
    List<TaskStatuses>  findByTaskStatuses ();
}