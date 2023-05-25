package com.example.MainProject.repository;

import com.example.MainProject.entities.users.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
