package com.bookmyconsultation.paymentservice.service;

import com.bookmyconsultation.paymentservice.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentService {
    Payment acceptPaymentForAppointment (String appointmentId, String authToken) throws JsonProcessingException;
}
