package com.example.MainProject.repository;

import com.example.MainProject.entities.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role>findByAuthority(String name);

    Boolean existsByAuthority(String admin);
}
