package com.starkindustries.Spring_Security1.service;

import com.starkindustries.Spring_Security1.dto.request.LoginDto;
import com.starkindustries.Spring_Security1.dto.response.LoginResponse;
import com.starkindustries.Spring_Security1.model.Users;
import com.starkindustries.Spring_Security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;

    public Users signup(Users users){
        String userId = UUID.randomUUID().toString();
        if(!this.userRepository.existsById(userId)){
            users.setUserId(userId);
            users.setPassword(this.bCryptPasswordEncoder.encode(users.getPassword()));
            return this.userRepository.save(users);
        }
        return null;
    }

    public LoginResponse login(LoginDto loginDto){

        Users users = this.userRepository.findByUsername(loginDto.getUsername());
        if(users!=null){
            Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            if(authentication.isAuthenticated()){
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setUsers(users);
                return loginResponse;
            }
        }
        return null;
    }
}
