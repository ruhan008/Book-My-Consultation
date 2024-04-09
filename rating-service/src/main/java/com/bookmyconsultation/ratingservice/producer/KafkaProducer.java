package com.bookmyconsultation.ratingservice.producer;

import com.bookmyconsultation.ratingservice.model.DoctorRating;
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
    private KafkaTemplate<String, DoctorRating> kafkaTemplate;

    @Value("${kafka.producer.topic}")
    private String topic;

    public void sendRatingEvent(DoctorRating doctorRating, String ratingEventType) throws JsonProcessingException {
        //Sending RatingEvent type as key so that consumer can parse based on type of message sent
        ListenableFuture<SendResult<String, DoctorRating>> listenableFuture = kafkaTemplate.send(topic, ratingEventType,
                doctorRating);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, DoctorRating>>() {

            @Override
            public void onSuccess(SendResult<String, DoctorRating> result) {
                handleSuccess(ratingEventType,doctorRating, result);
            }

            @Override
            public void onFailure(Throwable ex) {
                handleFailure(doctorRating, ex);
            }

        });
    }

    private void handleFailure(DoctorRating doctorRating, Throwable ex) {
        log.error("Error sending the message and exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(String key, DoctorRating doctorRating, SendResult<String, DoctorRating> result) {
        log.info("Message sent successfully to kafka for key: {} value: {} and the partition is {}",key, doctorRating,
                result.getRecordMetadata().partition());
    }

}
