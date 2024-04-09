package com.bookmyconsultation.appointmentservice.client;

import com.bookmyconsultation.appointmentservice.model.request.UserDetailsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://${USER_SERVICE_HOST:localhost}:8083")
public interface UserServiceClient {

    @GetMapping("users/{userId}/details")
    public UserDetailsRequest getUserDetailsRequest (@PathVariable String userId,
                                                     @RequestHeader(name = "Authorization") String authToken);

}
