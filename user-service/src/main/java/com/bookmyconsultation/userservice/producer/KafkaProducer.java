package com.bookmyconsultation.userservice.producer;

import com.bookmyconsultation.userservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    @Value("${kafka.producer.topic}")
    private String topic;

    public void sendUserEvent(User user, String userEventType) throws JsonProcessingException {
        //Sending UserEvent type as key so that consumer can parse based on type of message sent
        ListenableFuture<SendResult<String, User>> listenableFuture = kafkaTemplate.send(topic, userEventType,
                user);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, User>>() {

            @Override
            public void onSuccess(SendResult<String, User> result) {
                handleSuccess(userEventType,user, result);
            }

            @Override
            public void onFailure(Throwable ex) {
                handleFailure(user, ex);
            }

        });
    }

    private void handleFailure(User user, Throwable ex) {
        log.error("Error sending the message and exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(String key, User user, SendResult<String, User> result) {
        log.info("Message sent successfully to kafka for key: {} value: {} and the partition is {}",key, user,
                result.getRecordMetadata().partition());
    }

}
