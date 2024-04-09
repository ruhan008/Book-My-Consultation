package com.bookmyconsultation.notificationservice.service;

import freemarker.template.TemplateException;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EventProcessorService {
    void processIncomingMessage(ConsumerRecord<String,String> consumerRecord)
            throws IOException, TemplateException, MessagingException;
}
