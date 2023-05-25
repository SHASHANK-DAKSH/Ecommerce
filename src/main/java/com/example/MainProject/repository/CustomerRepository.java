package com.example.MainProject.repository;

import com.example.MainProject.entities.users.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long>, PagingAndSortingRepository<Customer,Long> {
    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

}
