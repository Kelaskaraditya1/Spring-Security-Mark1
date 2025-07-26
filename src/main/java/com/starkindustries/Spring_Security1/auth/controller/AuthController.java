package com.starkindustries.Spring_Security1.auth.controller;

import com.starkindustries.Spring_Security1.auth.dto.request.LoginDto;
import com.starkindustries.Spring_Security1.auth.dto.request.UpdatePasswordDto;
import com.starkindustries.Spring_Security1.auth.dto.response.LoginResponse;
import com.starkindustries.Spring_Security1.auth.model.UserPrinciple;
import com.starkindustries.Spring_Security1.auth.model.Users;
import com.starkindustries.Spring_Security1.auth.repository.UserRepository;
import com.starkindustries.Spring_Security1.email.service.EmailService;
import com.starkindustries.Spring_Security1.auth.service.JwtService;
import com.starkindustries.Spring_Security1.auth.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<?> getUSer(@PathVariable("userId") String userId){
        Users users = this.usersService.getUserById(userId);
        if(users!=null)
            return ResponseEntity.status(HttpStatus.OK).body(users);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist!!");
    }

    @GetMapping("/get-users")
    public ResponseEntity<?> getUsers(){
        List<Users> usersList = this.usersService.getUSers();
        if(!usersList.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(usersList);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Enter users first!!");
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto){

        Users users = this.usersService.updatePassword(updatePasswordDto);
        if(users!=null)
                return ResponseEntity.status(HttpStatus.OK).body(users);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Either Current password or User-Id is wrong!!");

    }
    

}
