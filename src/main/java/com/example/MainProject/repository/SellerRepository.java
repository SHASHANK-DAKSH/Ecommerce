package com.example.MainProject.repository;

import com.example.MainProject.entities.users.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Long>, PagingAndSortingRepository<Seller,Long> {
    Optional<Seller> findByEmail(String email);
    Boolean existsByEmail(String email);

   Boolean existsByCompanyNameIgnoreCase(String companyName);


    Boolean existsByGstNumberIgnoreCase(String gst);
}
