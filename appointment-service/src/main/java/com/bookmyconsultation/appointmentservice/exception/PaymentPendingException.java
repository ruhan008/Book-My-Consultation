package com.bookmyconsultation.appointmentservice.exception;

public class PaymentPendingException extends RuntimeException{
    public PaymentPendingException(String message) {
        super(message);
    }
}
