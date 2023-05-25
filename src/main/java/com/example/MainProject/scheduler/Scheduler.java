package com.example.MainProject.scheduler;

import com.example.MainProject.entities.users.User;
import com.example.MainProject.others.EmailSender;
import com.example.MainProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class Scheduler {
    @Autowired
    EmailSender emailSender;

    @Autowired
    UserRepository userRepository;


    @Scheduled(cron = "0 0 0 * * *")//(seconds,minute,hours,days,month,week) can use initial and fix delay also
    public void run(){
        List<User>user =userRepository.findAll();

        for(User u:user){
            if(!u.isActive()){
                emailSender.SimpleEmail(u.getEmail(),"Account Activation reminder",
                        "your account is in-Active please Activate with provided token");
                System.out.println("email send successfully");
            }
        }
    }
}
