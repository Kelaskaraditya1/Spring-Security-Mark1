package com.starkindustries.Spring_Security1.otp.service;


import com.starkindustries.Spring_Security1.keys.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class OtpService {


    public boolean sendOtp(String otp,String number){

        boolean status = false;

        try{

            String message = "Your OTP is: "+otp+". Please do not share this code with anyone. It is valid for the next 5 minutes.";

            message= URLEncoder.encode(message, StandardCharsets.UTF_8);

            String language = "english";

            String route  = "q";

//            String callUrl = "https://www.fast2sms.com/dev/bulkV2?"
//                    + "authorization=hIzXU5YcSBsLGTld3W0pt74qgex1mj2aJNAynD8ZvOMoRCHQf6kpP6ymqTlMdDFBZUAVhrbw7eXsGgQc"
//                    + "&sender_id=FSTSMS"
//                    + "&message=" + message
//                    + "&language=english"
//                    + "&route=q"
//                    + "&numbers=" + number;

            String callUrl="https://www.fast2sms.com/dev/bulkV2?authorization=hIzXU5YcSBsLGTld3W0pt74qgex1mj2aJNAynD8ZvOMoRCHQf6kpP6ymqTlMdDFBZUAVhrbw7eXsGgQc&route=q&message=I%2520am%2520Ironman&flash=0&numbers=8591059220&schedule_time=";

            log.error(callUrl);

            URL url = new URL(callUrl);

            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0");
            httpURLConnection.setRequestProperty("cache-control","no-cache");

            int requestCode = httpURLConnection.getResponseCode();

            StringBuffer response = new StringBuffer();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            while(true){
                String line = bufferedReader.readLine();
                if(line==null)
                    break;
                response.append(line);
            }

            log.error("response :{}",response);
            status = true;

        }catch (Exception e){
            log.error("Error Message: {}",e.getLocalizedMessage());
            e.printStackTrace();
        }

        return status;

    }
}
