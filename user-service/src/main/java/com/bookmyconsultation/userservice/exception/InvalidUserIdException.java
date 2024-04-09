package com.bookmyconsultation.userservice.exception;

public class InvalidUserIdException extends RuntimeException {

    public InvalidUserIdException(String message){
        super(message);
    }

}
