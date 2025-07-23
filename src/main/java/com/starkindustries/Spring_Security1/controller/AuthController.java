package com.starkindustries.Spring_Security1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/greetings")
    public ResponseEntity<?> greetings(){
        return ResponseEntity.status(HttpStatus.OK).body("Greetings,I am Optimus Prime!!");
    }


}
