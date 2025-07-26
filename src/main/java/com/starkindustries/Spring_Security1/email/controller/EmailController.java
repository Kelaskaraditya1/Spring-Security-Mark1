package com.starkindustries.Spring_Security1.email.controller;

import com.starkindustries.Spring_Security1.email.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Random;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    public EmailService emailService;

    @PostMapping("/sent-email/{email}")
    public ResponseEntity<?> sentEmail(@PathVariable("email") String email, HttpSession httpSession){

        Random random = new Random();
        int otp = 1000+random.nextInt(9000);

        if(this.emailService.sendEmail(email,otp,httpSession))
            return ResponseEntity.status(HttpStatus.OK).body("Email sent successfully!!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email!!");

    }

    @PostMapping("/validate-otp/{otp}")
    public ResponseEntity<?> validateOtp(HttpSession httpSession,@PathVariable("otp") String otp){

        int status=this.emailService.validateOtp(otp,httpSession);

        return switch (status) {
            case 0 -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid Otp");
            case 1 -> ResponseEntity.status(HttpStatus.OK).body("Email verified successfully!!");
            case -1 -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Otp expired!!");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

}
