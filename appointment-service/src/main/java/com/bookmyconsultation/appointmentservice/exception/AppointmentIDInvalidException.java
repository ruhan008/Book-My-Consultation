package com.bookmyconsultation.appointmentservice.exception;

public class AppointmentIDInvalidException extends RuntimeException{
    public AppointmentIDInvalidException(String message) {
        super(message);
    }
}
