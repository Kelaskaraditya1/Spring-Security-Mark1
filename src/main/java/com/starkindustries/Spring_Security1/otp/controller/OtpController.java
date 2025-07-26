package com.starkindustries.Spring_Security1.otp.controller;

import com.starkindustries.Spring_Security1.otp.service.OtpService;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    public OtpService otpService;

    @GetMapping("/send-otp")
    public ResponseEntity<?> sentOtp(){

        Random random = new Random();
        int otp = 100000+random.nextInt(900000);

        if(this.otpService.sendOtp(String.valueOf(otp),"8591059220"))
            return ResponseEntity.status(HttpStatus.OK).body("Message Sent Successfully!!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP!!");
    }

}
