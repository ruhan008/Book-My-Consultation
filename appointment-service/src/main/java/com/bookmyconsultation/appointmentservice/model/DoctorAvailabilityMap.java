package com.bookmyconsultation.appointmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorAvailabilityMap {
    private String doctorId;
    private Map<LocalDate, List<String>> availabilityMap;
}
