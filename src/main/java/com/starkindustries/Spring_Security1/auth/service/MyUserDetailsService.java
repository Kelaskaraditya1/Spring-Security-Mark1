package com.starkindustries.Spring_Security1.auth.service;

import com.starkindustries.Spring_Security1.auth.model.UserPrinciple;
import com.starkindustries.Spring_Security1.auth.model.Users;
import com.starkindustries.Spring_Security1.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = this.userRepository.findByUsername(username);
        if(users!=null){
            UserPrinciple userPrinciple = new UserPrinciple(users);
            return userPrinciple;
        }
        return null;
    }
}
