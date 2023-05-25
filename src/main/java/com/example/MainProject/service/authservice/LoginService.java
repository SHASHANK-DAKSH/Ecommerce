package com.example.MainProject.service.authservice;

import com.example.MainProject.dto.LoginDto;
import com.example.MainProject.dto.MessageDto;
import com.example.MainProject.entities.token.Tokens;
import com.example.MainProject.entities.users.User;
import com.example.MainProject.exception.GenericException;
import com.example.MainProject.others.EmailSender;
import com.example.MainProject.repository.RoleRepository;
import com.example.MainProject.repository.TokenRepo;
import com.example.MainProject.repository.UserRepository;
import com.example.MainProject.security.JwtGenerator;
import com.example.MainProject.service.authservice.Impl.LoginServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginService implements LoginServiceImpl {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;
    private EmailSender emailSender;
    private TokenRepo tokenRepo;

    @Autowired

    public LoginService(AuthenticationManager authenticationManager,
                        UserRepository userRepository, RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator, EmailSender emailSender,
                        TokenRepo tokenRepo) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator=jwtGenerator;
        this.emailSender=emailSender;
        this.tokenRepo=tokenRepo;
    }


    Logger logger= LoggerFactory.getLogger(LoginService.class);



    @Override
    public MessageDto loginCustomerDb(LoginDto loginDto) {

        User user = userRepository.findByEmail(loginDto.getEmail()).orElse(null);
        if(user==null){
            logger.warn("user is not present in DB");
            throw new GenericException("no user registered with this email"
                    ,HttpStatus.NOT_FOUND);
        }

        if(!passwordEncoder.matches(loginDto.getPassword(),user.getPassword())) {
            if (user.getInvalidAttemptCount() == 2) {
                logger.info("your account is locked");
                user.setLocked(true);
                userRepository.save(user);
                emailSender.SimpleEmail(loginDto.getEmail(), "account locked", "your account is locked");

                throw new GenericException("Account Locked", HttpStatus.LOCKED);
            } else {
                logger.warn("invalid password for "+user.getInvalidAttemptCount()+1);
                user.setInvalidAttemptCount(user.getInvalidAttemptCount() + 1);
                userRepository.save(user);
                throw new GenericException("Wrong Password", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if(passwordEncoder.matches(loginDto.getPassword(),user.getPassword())) {
            logger.info("password match correct: save the user");
            user.setInvalidAttemptCount(0);
            userRepository.save(user);
        }

        if(!user.isActive()|| user.isLocked()){
            throw new GenericException("user is Locked or not Active",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Boolean isToken=tokenRepo.existsByEmail(loginDto.getEmail());
        if(isToken){
            Tokens tokens=tokenRepo.findByEmail(loginDto.getEmail()).orElse(null);
            String token= tokens.getToken();
//            if(jwtGenerator.validateToken(token)){
            if(tokens.getExpiration().after(new Date())){
                return new MessageDto("you are already logged in  ",token);
            }
            else{
                tokenRepo.deleteByEmail(loginDto.getEmail());
                Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String NewToken = jwtGenerator.generateToken(authentication);
                tokens.setToken(NewToken);
                tokens.setEmail(loginDto.getEmail());
                tokens.setExpiration(jwtGenerator.getExpiration(NewToken));
                tokenRepo.save(tokens);
                return new MessageDto("successfully logged in ",NewToken);

            }
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        Tokens tokens=new Tokens();
        tokens.setToken(token);
        tokens.setEmail(loginDto.getEmail());
        tokens.setExpiration(jwtGenerator.getExpiration(token));
        tokenRepo.save(tokens);

        return new MessageDto("successfully logged in",token);

    }
}









































//     Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token = jwtGenerator.generateToken(authentication);
//
//
//        // if token don't exist
//        if(!tokenRepo.existsByEmail(loginDto.getEmail())) {
//            Tokens tokens = new Tokens();
//            tokens.setToken(token);
//            tokens.setEmail(loginDto.getEmail());
//            tokenRepo.save(tokens);
//            return new ResponseEntity<>("successfully logged in  "+token, HttpStatus.OK);
//        }
//        else{
//            Tokens tokens=tokenRepo.findByEmail(loginDto.getEmail()).orElse(null);
//           Boolean expire=jwtGenerator.validateToken(tokens.getToken());
//           // token exist but expire
//           if(expire){
//               tokens.setToken(token);
//               tokens.setEmail(loginDto.getEmail());
//               tokenRepo.save(tokens);
//               return new ResponseEntity<>("successfully logged in  "+token, HttpStatus.OK);
//           }
//           else{// token is valid so dont send again
//               return new ResponseEntity<>("you are already logged in"+tokens.getToken(),HttpStatus.OK);
//           }
//        }
////        else {
////            Tokens tokens=tokenRepo.findByEmail(loginDto.getEmail()).orElse(null);
////            tokens.setToken(token);
////            tokenRepo.save(tokens);
////            return new ResponseEntity<>("Already logged in "+token,HttpStatus.OK);
////        }
