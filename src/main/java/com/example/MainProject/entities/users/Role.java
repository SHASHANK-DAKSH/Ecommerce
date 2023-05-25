package com.example.MainProject.entities.users;

import com.example.MainProject.entities.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Role extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String authority;

    @ManyToMany(mappedBy ="role")
    private List<User> user;
}
