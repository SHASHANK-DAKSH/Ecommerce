package com.example.MainProject.others;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class EmailSender {
    @Autowired
    JavaMailSender javaMailSender;

    public void SimpleEmail(String toMail,String subject,String body){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("shashank.daksh@tothenew.com");
        message.setTo(toMail);
        message.setText(body);
        message.setSubject(subject);


        javaMailSender.send(message);
        System.out.println("mail send successfully");
    }
}
