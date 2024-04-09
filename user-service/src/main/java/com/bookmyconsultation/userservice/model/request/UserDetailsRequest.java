package com.bookmyconsultation.userservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsRequest {
    private String userId;
    private String userName;
    private String emailId;
}