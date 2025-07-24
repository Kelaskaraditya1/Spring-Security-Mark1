package com.starkindustries.Spring_Security1.security.configuration;

import com.starkindustries.Spring_Security1.filter.JwtAuthenticationFilter;
import com.starkindustries.Spring_Security1.service.MyUserDetailsService;
import io.netty.util.internal.NoOpTypeParameterMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    public MyUserDetailsService myUserDetailsService;

    @Autowired
    public JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder getBcryptPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{

        return httpSecurity.csrf(csrf->csrf.disable())
                .cors(cors->cors.disable())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(request->
                        request.requestMatchers("/auth/login/**","/auth/signup/**","/auth/validate/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

//    @Bean
//    public UserDetailsService getUserDetails(){
//
//        UserDetails userDetails1 = User
//                .withDefaultPasswordEncoder()
//                .username("kelaskaraditya1")
//                .password("Aditya@12345")
//                .roles("USER")
//                .build();
//
//        UserDetails userDetails2 = User
//                .withDefaultPasswordEncoder()
//                .username("mayur1")
//                .password("Mayur@1234")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(List.of(userDetails1,userDetails2));
//    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(getBcryptPasswordEncoder());;
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
