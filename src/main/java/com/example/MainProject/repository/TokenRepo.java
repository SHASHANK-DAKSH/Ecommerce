package com.example.MainProject.repository;

import com.example.MainProject.entities.token.Tokens;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Tokens,Long> {
    @Modifying
    @Transactional
    void deleteByEmail(String email);

    Optional<Tokens> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByToken(String token);

    Optional<Tokens>findByToken(String token);
}
