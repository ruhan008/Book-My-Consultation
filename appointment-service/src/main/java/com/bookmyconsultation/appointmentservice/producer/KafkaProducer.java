package com.bookmyconsultation.appointmentservice.producer;

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
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.producer.topic}")
    private String topic;

    public void sendEvent(String eventType, String message) {
        //Sending event type as key so that consumer can parse based on type of message sent
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(topic, eventType,
                message);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                handleSuccess(eventType,message, result);
            }

            @Override
            public void onFailure(Throwable ex) {
                handleFailure(message, ex);
            }

        });
    }

    private void handleFailure(String message, Throwable ex) {
        log.error("Error sending the message and exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(String key, String message, SendResult<String, String> result) {
        log.info("Message sent successfully to kafka for key: {} value: {} and the partition is {}",key, message,
                result.getRecordMetadata().partition());
    }
    
}
