package com.example.MainProject.bootstrap;

import com.example.MainProject.entities.users.Role;
import com.example.MainProject.entities.users.User;
import com.example.MainProject.repository.RoleRepository;
import com.example.MainProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;

@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Value("${admin.username}")
    String email;

    @Value("${admin.password}")
    String password;


    @Override
    public void run(String... args) throws Exception {
        if(!roleRepository.existsByAuthority("ADMIN")){
            Role role=new Role();
            role.setAuthority("ADMIN");
            roleRepository.save(role);
        }
        if(!roleRepository.existsByAuthority("SELLER")){
            Role role=new Role();
            role.setAuthority("SELLER");
            roleRepository.save(role);
        }

        if(!roleRepository.existsByAuthority("CUSTOMER")){
            Role role=new Role();
            role.setAuthority("CUSTOMER");
            roleRepository.save(role);
        }

        if(!userRepository.existsByEmail(email)){
            User user=new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setFirstName("SHASHANK");
            user.setLastname("DAKSH");
            user.setDeleted(false);
            user.setActive(true);
            user.setExpired(false);
            user.setLocked(false);
            user.setInvalidAttemptCount(0);
            user.setPasswordUpdateDate(new Date());

            Role role =roleRepository.findByAuthority("ADMIN").orElse(null);
            user.setRole(Collections.singletonList(role));
            userRepository.save(user);
        }

    }
}








//="shashank.daksh10@gmail.com";
//="Daksh@10";