package com.bookmyconsultation.appointmentservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExceptionResponse {
    private String timestamp;
    private int status;
    private String error;
    private String path;
}
