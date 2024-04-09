package com.bookmyconsultation.paymentservice.service;

import com.bookmyconsultation.paymentservice.client.AppointmentServiceClient;
import com.bookmyconsultation.paymentservice.model.Payment;
import com.bookmyconsultation.paymentservice.model.request.Appointment;
import com.bookmyconsultation.paymentservice.producer.KafkaProducer;
import com.bookmyconsultation.paymentservice.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.bookmyconsultation.paymentservice.constants.PaymentConstants.APPOINTMENT_PAYMENT_EVENT;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AppointmentServiceClient appointmentServiceClient;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Payment acceptPaymentForAppointment(String appointmentId, String authToken) throws JsonProcessingException {
        Payment payment = Payment.builder()
                .appointmentId(appointmentId)
                .createdDate(LocalDateTime.now())
                .build();
        // Save the payment received in db first
        Payment savedPayment = paymentRepository.save(payment);
        // Communicate with appointment-service using open-feign to update the payment status for the appointment
        appointmentServiceClient.updatePaymentStatus(appointmentId, authToken);
        /* Once payment status is updated get the user details from appointment-service for sending email to user
           for payment update. I have kept this as a separate call since payment update if more important and
           the notification can be sent with none or minor delays post update */
        Appointment paymentStatusUpdatedAppointment = appointmentServiceClient.getAppointmentDetails(appointmentId,
                        authToken)
                .getBody();
        savedPayment.setUserName(paymentStatusUpdatedAppointment.getUserName());
        savedPayment.setUserEmailId(paymentStatusUpdatedAppointment.getUserEmailId());
        // Updating payment record with user details to avoid future calls for the same
        Payment userDetailsUpdatedPayment = paymentRepository.save(savedPayment);
        // Sending payment object to notification-service for email on payment update
        kafkaProducer.sendPaymentEvent(APPOINTMENT_PAYMENT_EVENT, userDetailsUpdatedPayment);
        return userDetailsUpdatedPayment;
    }
}
