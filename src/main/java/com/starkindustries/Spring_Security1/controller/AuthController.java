package com.starkindustries.Spring_Security1.controller;

import com.starkindustries.Spring_Security1.dto.request.LoginDto;
import com.starkindustries.Spring_Security1.dto.response.LoginResponse;
import com.starkindustries.Spring_Security1.model.UserPrinciple;
import com.starkindustries.Spring_Security1.model.Users;
import com.starkindustries.Spring_Security1.repository.UserRepository;
import com.starkindustries.Spring_Security1.service.EmailService;
import com.starkindustries.Spring_Security1.service.JwtService;
import com.starkindustries.Spring_Security1.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.HttpAccessor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    public UsersService usersService;

    @Autowired
    public JwtService jwtService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public EmailService emailService;

    @GetMapping("/greetings")
    public ResponseEntity<?> greetings(){
        return ResponseEntity.status(HttpStatus.OK).body("Greetings,I am Optimus Prime!!");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Users users){
        Users users1 = this.usersService.signup(users);
        if(users1!=null)
            return ResponseEntity.status(HttpStatus.OK).body(users1);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to signup!!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){

        LoginResponse loginResponse = this.usersService.login(loginDto);
        if(loginResponse!=null)
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to loogin!!");

    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader){

        Map<String,Object> response = new HashMap<>();

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            response.put("valid",false);
            response.put("message","Auth header is null or it doesn't start with Bearer");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String jwtToken = authHeader.substring(7);
        String username="";

        try{
            username = this.jwtService.extractUserName(jwtToken);
        }catch (Exception e){
            e.printStackTrace();
            response.put("valid", false);
            response.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Users users = this.userRepository.findByUsername(username);

        if(users==null){
            response.put("valid",false);
            response.put("message","User doesn't exist in database");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        UserDetails userDetails = new UserPrinciple(users);

        boolean status = this.jwtService.isTokenValid(jwtToken,userDetails);

        response.put("valid",status);
        response.put("message",status?"Issue in UserDetails":"Token validated Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping("/sent-email/{email}")
    public ResponseEntity<?> sentEmail(@PathVariable("email") String email){

        Random random = new Random();
        int otp = 1000+random.nextInt(9000);

        if(this.emailService.sendEmail(email,otp))
            return ResponseEntity.status(HttpStatus.OK).body("Email sent successfully!!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email!!");

    }

}
