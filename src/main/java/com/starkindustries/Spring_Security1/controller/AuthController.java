package com.starkindustries.Spring_Security1.controller;

import com.starkindustries.Spring_Security1.dto.request.LoginDto;
import com.starkindustries.Spring_Security1.dto.response.LoginResponse;
import com.starkindustries.Spring_Security1.model.Users;
import com.starkindustries.Spring_Security1.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    public UsersService usersService;

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


}
