package com.example.MainProject.entities.users;

import com.example.MainProject.entities.audit.Auditable;
import com.example.MainProject.entities.users.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String email;

    private String firstName;

    private String middleName;

    private String lastname;

    private String password;

    private boolean isDeleted;

    private boolean isActive;

    private boolean isExpired;

    private boolean isLocked;

    private int invalidAttemptCount;

    private Date passwordUpdateDate;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Role> role;
    private String imagePath;
}
