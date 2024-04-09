package com.bookmyconsultation.notificationservice.service;

import com.bookmyconsultation.notificationservice.model.Appointment;
import com.bookmyconsultation.notificationservice.model.Doctor;
import com.bookmyconsultation.notificationservice.model.Payment;
import com.bookmyconsultation.notificationservice.model.Prescription;
import com.bookmyconsultation.notificationservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

import static com.bookmyconsultation.notificationservice.constants.NotificationConstants.APPOINTMENT_EVENTS;
import static com.bookmyconsultation.notificationservice.constants.NotificationConstants.DOCTOR_EVENTS;
import static com.bookmyconsultation.notificationservice.constants.NotificationConstants.PAYMENT_EVENTS;
import static com.bookmyconsultation.notificationservice.constants.NotificationConstants.USER_EVENTS;

@Service
@Slf4j
public class EventProcessorServiceImpl implements EventProcessorService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmailService emailService;

    @Override
    public void processIncomingMessage(ConsumerRecord<String, String> consumerRecord) throws IOException, TemplateException,
            MessagingException {
        if (DOCTOR_EVENTS.contains(consumerRecord.key())) {
            processDoctorEvent(consumerRecord);
        } else if (APPOINTMENT_EVENTS.contains(consumerRecord.key())) {
            processAppointmentEvent(consumerRecord);
        } else if (PAYMENT_EVENTS.contains(consumerRecord.key())) {
            processPaymentEvent(consumerRecord);
        } else if (USER_EVENTS.contains(consumerRecord.key())) {
            processUserEvent(consumerRecord);
        }
    }

    private void processUserEvent(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        User user = objectMapper.readValue(consumerRecord.value(), User.class);
        String eventType = consumerRecord.key();
        switch (eventType) {
            case "UserRegistrationEvent":
                log.info("User registration received: {}", user);
                break;
            default:
                break;
        }
    }

    private void processPaymentEvent(ConsumerRecord<String, String> consumerRecord) throws IOException, TemplateException,
            MessagingException {
        Payment payment = objectMapper.readValue(consumerRecord.value(), Payment.class);
        String eventType = consumerRecord.key();
        switch (eventType) {
            case "AppointmentPaymentEvent":
                log.info("Sending email notification to {} with payment details: {}", payment.getUserName(), payment);
                emailService.sendEmailForPaymentEvent(eventType, payment);
                break;
            default:
                break;
        }
    }

    private void processAppointmentEvent(ConsumerRecord<String, String> consumerRecord) throws IOException,
            TemplateException, MessagingException {
        String eventType = consumerRecord.key();
        switch (eventType) {
            case "UserAppointmentEvent":
                Appointment appointment = objectMapper.readValue(consumerRecord.value(),
                        Appointment.class);
                log.info("Sending email to user {} for appointment confirmation", appointment.getUserName());
                emailService.sendEmailForUserEvent(eventType, appointment);
                break;
            case "PrescriptionUploadEvent":
                Prescription prescription = objectMapper.readValue(consumerRecord.value(), Prescription.class);
                emailService.sendEmailForPrescriptionEvent(eventType, prescription);
        }
    }

    private void processDoctorEvent(ConsumerRecord<String, String> consumerRecord) throws IOException, TemplateException,
            MessagingException {
        Doctor doctor = objectMapper.readValue(consumerRecord.value(), Doctor.class);
        String eventType = consumerRecord.key();
        switch (eventType) {
            case "DoctorRegistration":
                log.info("Doctor registration received: {}", doctor);
                break;
            case "DoctorApprovalEvent":
            case "DoctorRejectionEvent":
                log.info("Sending email for {} to {} {}", eventType,doctor.getFirstName(),doctor.getLastName());
                emailService.sendEmailForDoctorEvent(doctor, eventType);
                break;
            default:
                break;
        }
    }
}
