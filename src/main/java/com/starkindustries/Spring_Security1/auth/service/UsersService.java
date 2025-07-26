package com.starkindustries.Spring_Security1.auth.service;

import com.starkindustries.Spring_Security1.auth.dto.request.LoginDto;
import com.starkindustries.Spring_Security1.auth.dto.request.UpdatePasswordDto;
import com.starkindustries.Spring_Security1.auth.dto.response.LoginResponse;
import com.starkindustries.Spring_Security1.auth.model.Users;
import com.starkindustries.Spring_Security1.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UsersService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public JwtService jwtService;


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
                String jwtToken = this.jwtService.getJwtToken(users);
                if(!jwtToken.isEmpty())
                    loginResponse.setJwtToken(jwtToken);
                return loginResponse;
            }
        }
        return null;
    }

    public Users getUserById(String userId){
        if(this.userRepository.existsById(userId)){
            Users users = this.userRepository.findById(userId).get();
            return users;
        }
        return null;
    }

    public List<Users> getUSers(){
        return this.userRepository.findAll();
    }

    public Users updatePassword(UpdatePasswordDto updatePasswordDto){

        if(this.userRepository.existsById(updatePasswordDto.getUserId())){

            Users users = this.userRepository.findById(updatePasswordDto.getUserId()).get();

            log.error("User org password: {}",users.getPassword());

            if(this.bCryptPasswordEncoder.matches(updatePasswordDto.getCurrentPassword(), users.getPassword())){
                users.setPassword(this.bCryptPasswordEncoder.encode(updatePasswordDto.getNewPassword()));
                return this.userRepository.save(users);
            }
        }
        return null;
    }
}
