package com.bookmyconsultation.doctorservice.model.request;

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
public class DoctorDetailsRequest {
    private String doctorId;
    private String doctorName;
    private String doctorEmailId;
}
