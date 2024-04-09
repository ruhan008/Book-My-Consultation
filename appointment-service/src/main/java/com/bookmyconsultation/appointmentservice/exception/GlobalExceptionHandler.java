package com.bookmyconsultation.appointmentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppointmentIDInvalidException.class)
    public ResponseEntity<ErrorModel> handleAppointmentIDInvalidException(AppointmentIDInvalidException ex) {
        return new ResponseEntity<>(ErrorModel.builder()
                .errorCode("ERR_RESOURCE_NOT_FOUND")
                .errorMessage(ex.getMessage())
                .errorFields(null).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentPendingException.class)
    public ResponseEntity<ErrorModel> handleAppointmentIDInvalidException(PaymentPendingException ex) {
        return new ResponseEntity<>(ErrorModel.builder()
                .errorCode("ERR_PAYMENT_PENDING")
                .errorMessage(ex.getMessage())
                .errorFields(null).build(), HttpStatus.BAD_REQUEST);
    }
}
