package com.starkindustries.Spring_Security1.service;

import com.starkindustries.Spring_Security1.keys.Keys;
import com.starkindustries.Spring_Security1.model.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import jakarta.websocket.Decoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class JwtService {

    public String secreteKey = null;

    public String getSecreteKey(){
        secreteKey = Keys.SECRETE_KEY;
        return secreteKey;
    }

    public String getJwtToken(Users users){

        Map<String,Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(users.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ (1000*60)))
                .and()
                .signWith(generateKey())
                .compact();
    }

    public SecretKey generateKey(){
        getSecreteKey();
        byte[] data = Decoders.BASE64.decode(secreteKey);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(data);
    }
}
