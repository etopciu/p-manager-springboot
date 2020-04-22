package com.ikubinfo.projectmanager.repository;

import java.util.Optional;

import com.ikubinfo.projectmanager.models.ERole;
import com.ikubinfo.projectmanager.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
