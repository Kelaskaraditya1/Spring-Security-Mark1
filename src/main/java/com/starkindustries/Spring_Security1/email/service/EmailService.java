package com.starkindustries.Spring_Security1.email.service;

import com.starkindustries.Spring_Security1.keys.Keys;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Properties;

@Slf4j
@Service
public class EmailService {

    public boolean sendEmail(String email, int otp, HttpSession httpSession){

        var emailBody="Thank you for signing up! To complete your email verification, please use the One-Time Password (OTP) below:\n" +
                "\n" +
                "Your OTP: "+ otp +"\n" +
                "\n" +
                "This OTP is valid for the next 10 minutes. Please do not share it with anyone.\n" +
                "\n" +
                "If you did not request this verification, please ignore this email.\n" +
                "\n" +
                "Best regards";

        boolean status = false;

        Properties properties = System.getProperties();

        properties.put(Keys.HOST, "smtp.gmail.com");
        properties.put(Keys.PORT, "465");
        properties.put(Keys.AUTH, "true");
        properties.put(Keys.SSL_ENABLED, "true");
        properties.put(Keys.SOCKET_FACTORY_PORT, "465");
        properties.put(Keys.SOCKET_FACTORY_CLASS, "javax.net.ssl.SSLSocketFactory");
        properties.put(Keys.SOCKET_FACTORY_FALLBACK, "false");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Keys.APP_PASSWORD_EMAIL,Keys.APP_PASSWORD);
            }
        });

        session.setDebug(true);

        try{
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setSubject("Verify for email for secure access!!");
            mimeMessage.setText(emailBody);
            mimeMessage.setFrom(Keys.APP_PASSWORD_EMAIL);
            mimeMessage.addRecipients(MimeMessage.RecipientType.TO, String.valueOf(new InternetAddress(email)));
            Transport.send(mimeMessage);
            httpSession.removeAttribute(Keys.OTP);
            httpSession.removeAttribute(Keys.OTP_TIME);
            httpSession.setAttribute(Keys.OTP,otp);
            httpSession.setAttribute(Keys.OTP_TIME, Instant.now());
            status=true;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("message: {}",e.getLocalizedMessage());
        }

        return status;

    }

    public int validateOtp(String otp , HttpSession httpSession){

        String sessionOtp = httpSession.getAttribute(Keys.OTP).toString();

        Instant otpTime =(Instant) httpSession.getAttribute(Keys.OTP_TIME);

        if(sessionOtp==null && otpTime==null)
            return -1;
        else{

            if(Duration.between(Instant.now(),otpTime).toMinutes()>5){
                httpSession.removeAttribute(Keys.OTP);
                httpSession.removeAttribute(Keys.OTP_TIME);
                return -1;
            }else {
                if(otp.equals(sessionOtp)) {
                    httpSession.removeAttribute(Keys.OTP);
                    httpSession.removeAttribute(Keys.OTP_TIME);
                    return 1;
                }
                else
                    return 0;
            }
        }

    }

}
