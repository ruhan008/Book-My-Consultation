package com.bookmyconsultation.userservice.service;

import com.bookmyconsultation.userservice.exception.InvalidUserIdException;
import com.bookmyconsultation.userservice.model.User;
import com.bookmyconsultation.userservice.model.request.UserDetailsRequest;
import com.bookmyconsultation.userservice.producer.KafkaProducer;
import com.bookmyconsultation.userservice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.bookmyconsultation.userservice.constant.UserConstants.USER_REGISTRATION_EVENT;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private EmailService emailService;

    @Override
    public User registerNewUser(User user) throws JsonProcessingException {
        processUserDetailsReceived(user);
        User savedUser = userRepository.save(user);
        emailService.verifyEmail(user.getEmailId());
        kafkaProducer.sendUserEvent(user, USER_REGISTRATION_EVENT);
        return savedUser;
    }

    @Override
    public User getUserDetails(String userId) {
        Optional<User> checkUserExistence = userRepository.findById(userId);
        if (checkUserExistence.isPresent()) {
            return checkUserExistence.get();
        }
        throw new InvalidUserIdException("Requested resource is not valid");
    }

    @Override
    public UserDetailsRequest getUserDetailsRequest(String userId) {
        User user = userRepository.findById(userId).get();
        return UserDetailsRequest.builder()
                .userId(userId)
                .userName(user.getFirstName() + " " + user.getLastName())
                .emailId(user.getEmailId())
                .build();
    }

    private void processUserDetailsReceived(User user) {
        user.setCreatedDate(LocalDate.now());
    }
}
