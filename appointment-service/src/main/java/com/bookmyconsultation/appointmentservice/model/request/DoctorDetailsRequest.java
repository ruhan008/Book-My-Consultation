package com.bookmyconsultation.appointmentservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDetailsRequest {
    private String doctorId;
    private String doctorName;
    private String doctorEmailId;
}
