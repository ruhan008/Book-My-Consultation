package com.bookmyconsultation.doctorservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private SesClient sesClient;

    @PostConstruct
    public void init(){
        // When you hit the endpoint to verify the email this needs to be the ses access key for your AWS account
        String accessKey = ""; // Configure before run
        // When you hit the endpoint to verify the email this needs to be the ses secret key for your AWS account
        String secretKey = ""; // Configure before run

        //This is required for email verification
        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider
                .create(AwsBasicCredentials.create(accessKey,secretKey));
        sesClient = SesClient.builder()
                .credentialsProvider(staticCredentialsProvider)
                .region(Region.US_EAST_1)
                .build();
    }

    public void verifyEmail(String emailId){
        sesClient.verifyEmailAddress(req->req.emailAddress(emailId));
        log.info("Email verification sent to {} successfully", emailId);
    }

}
