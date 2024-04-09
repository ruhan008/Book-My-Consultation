package com.bookmyconsultation.doctorservice.producer;

import com.bookmyconsultation.doctorservice.model.Doctor;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static com.bookmyconsultation.doctorservice.constant.DoctorConstants.DOCTOR_REGISTRATION_EVENT;

@Component
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Doctor> kafkaTemplate;

    @Value("${kafka.producer.topic}")
    private String topic;

    public void sendDoctorEvent(Doctor doctor, String doctorEventType) throws JsonProcessingException {
        //Sending DoctorEvent type as key so that consumer can parse based on type of message sent
        ListenableFuture<SendResult<String, Doctor>> listenableFuture = kafkaTemplate.send(topic, doctorEventType,
                doctor);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, Doctor>>() {

            @Override
            public void onSuccess(SendResult<String, Doctor> result) {
                handleSuccess(doctorEventType,doctor, result);
            }

            @Override
            public void onFailure(Throwable ex) {
                handleFailure(doctor, ex);
            }

        });
    }

    private void handleFailure(Doctor doctor, Throwable ex) {
        log.error("Error sending the message and exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(String key, Doctor doctor, SendResult<String, Doctor> result) {
        log.info("Message sent successfully to kafka for key: {} value: {} and the partition is {}",key, doctor,
                result.getRecordMetadata().partition());
    }


}
