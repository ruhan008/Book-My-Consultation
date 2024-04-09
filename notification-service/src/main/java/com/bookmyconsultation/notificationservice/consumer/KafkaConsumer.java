package com.bookmyconsultation.notificationservice.consumer;

import com.bookmyconsultation.notificationservice.service.EventProcessorService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    private EventProcessorService eventProcessorService;

    @KafkaListener(topics = {"${kafka.consumer.topic}"},containerFactory = "concurrentKafkaListenerContainerFactory")
    public void OnMessage(ConsumerRecord<String,String> consumerRecord) throws TemplateException, MessagingException,
            IOException {
        log.info("Received event:{} with message:{}",consumerRecord.key(), consumerRecord.value());
        eventProcessorService.processIncomingMessage(consumerRecord);
    }
}
