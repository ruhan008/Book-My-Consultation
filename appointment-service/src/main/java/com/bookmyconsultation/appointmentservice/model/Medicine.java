package com.bookmyconsultation.appointmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {
    private String name;
    private String dosage;
    private String frequency;
    private String remarks;
}
