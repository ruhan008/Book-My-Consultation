package com.bookmyconsultation.doctorservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorModel> handleInvalidArguements(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ErrorModel.builder()
                .errorCode("ERR_INVALID_INPUT")
                .errorMessage("Invalid input. Parameter name: ")
                .errorFields(ex.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> fieldError.getField() + " - " + fieldError.getDefaultMessage())
                        .collect(Collectors.toList())).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDoctorIdException.class)
    public ResponseEntity<ErrorModel> handleInvalidDoctorIdException(InvalidDoctorIdException ex) {
        return new ResponseEntity<>(ErrorModel.builder()
                .errorCode("ERR_RESOURCE_NOT_FOUND")
                .errorMessage(ex.getMessage())
                .errorFields(null).build(), HttpStatus.NOT_FOUND);
    }

}
