package com.bookmyconsultation.appointmentservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsRequest {
    private String userId;
    private String userName;
    private String emailId;
}
