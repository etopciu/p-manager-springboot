package com.ikubinfo.projectmanager.repository;

import java.util.List;
import java.util.Optional;

import com.ikubinfo.projectmanager.models.User;
import com.ikubinfo.projectmanager.payload.response.ActiveUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	User findByTasks_Id(Integer id);
	@Query(value = "SELECT new com.ikubinfo.projectmanager.payload.response.ActiveUsers( us.username, count (us.id))  FROM User us JOIN us.tasks ts GROUP BY us.id  ")
	List<ActiveUsers> findMostActiveUsers();
}
