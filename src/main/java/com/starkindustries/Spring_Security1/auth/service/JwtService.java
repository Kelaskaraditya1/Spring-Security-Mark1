package com.starkindustries.Spring_Security1.auth.service;

import com.starkindustries.Spring_Security1.keys.Keys;
import com.starkindustries.Spring_Security1.auth.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    public String secreteKey = "";

//    public String getSecreteKey(){
//        secreteKey = Keys.SECRETE_KEY;
//        return secreteKey;
//    }

    public JwtService(){
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance(Keys.HMAC_SHA_256);
            SecretKey sk = keyGenerator.generateKey();
            secreteKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        }catch (Exception e){
            e.printStackTrace();
            log.error("secret key error: {}",e.getLocalizedMessage());
        }
    }

    public String getJwtToken(Users users){

        Map<String,Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(users.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+(60*1000)))
                .and()
                .signWith(generateKey())
                .compact();
    }

    public SecretKey generateKey(){
        byte[] data = Decoders.BASE64.decode(secreteKey);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(data);
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);

    }

    private <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        final boolean isExpired = isTokenExpired(token);

        log.info("Validating token for user...");
        log.info("→ Extracted username from token : {}", userName);
        log.info("→ UserDetails username         : {}", userDetails.getUsername());
        log.info("→ Token expired?                : {}", isExpired);

        boolean isValid = userName.equals(userDetails.getUsername()) && !isExpired;
        log.info("→ Final token validity result   : {}", isValid);

        return isValid;
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}
