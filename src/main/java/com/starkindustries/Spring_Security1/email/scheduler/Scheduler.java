package com.starkindustries.Spring_Security1.email.scheduler;

import com.starkindustries.Spring_Security1.keys.Keys;
import jakarta.servlet.http.HttpSession;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class Scheduler {

    @Scheduled(fixedRate = 3000000)
    public void removeOtp(HttpSession httpSession){
        httpSession.removeAttribute(Keys.OTP);
        httpSession.removeAttribute(Keys.OTP_TIME);
    }

}
