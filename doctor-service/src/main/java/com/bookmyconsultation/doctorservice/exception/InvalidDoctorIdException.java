package com.bookmyconsultation.doctorservice.exception;

public class InvalidDoctorIdException extends RuntimeException {
    public InvalidDoctorIdException(String message) {
        super(message);
    }
}
