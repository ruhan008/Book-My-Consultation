package com.bookmyconsultation.userservice.service;

import com.bookmyconsultation.userservice.model.User;
import com.bookmyconsultation.userservice.model.request.UserDetailsRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserService {

    User registerNewUser(User user) throws JsonProcessingException;

    User getUserDetails(String userId);

    UserDetailsRequest getUserDetailsRequest(String userId);
}
