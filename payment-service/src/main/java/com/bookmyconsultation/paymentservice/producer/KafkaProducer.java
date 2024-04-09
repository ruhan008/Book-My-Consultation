package com.bookmyconsultation.paymentservice.producer;

import com.bookmyconsultation.paymentservice.model.Payment;
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
    private KafkaTemplate<String, Payment> kafkaTemplate;

    @Value("${kafka.producer.topic}")
    private String topic;

    public void sendPaymentEvent(String paymentEventType, Payment payment) throws JsonProcessingException {
        //Sending PaymentEvent type as key so that consumer can parse based on type of message sent
        ListenableFuture<SendResult<String, Payment>> listenableFuture = kafkaTemplate.send(topic, paymentEventType,
                payment);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, Payment>>() {

            @Override
            public void onSuccess(SendResult<String, Payment> result) {
                handleSuccess(paymentEventType,payment, result);
            }

            @Override
            public void onFailure(Throwable ex) {
                handleFailure(payment, ex);
            }

        });
    }

    private void handleFailure(Payment payment, Throwable ex) {
        log.error("Error sending the message and exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(String key, Payment payment, SendResult<String, Payment> result) {
        log.info("Message sent successfully to kafka for key: {} value: {} and the partition is {}",key, payment,
                result.getRecordMetadata().partition());
    }


}
