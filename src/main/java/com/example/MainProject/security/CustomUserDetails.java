package com.example.MainProject.security;

import com.example.MainProject.entities.users.Role;
import com.example.MainProject.entities.users.User;
import com.example.MainProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetails implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException("No email found"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),mapRolesToAuthority(user.getRole()));
    }
    private Collection<GrantedAuthority> mapRolesToAuthority(List<Role>roles){
        return roles.stream().map(role ->new SimpleGrantedAuthority(role.getAuthority())).
                collect(Collectors.toList());
    }

}
