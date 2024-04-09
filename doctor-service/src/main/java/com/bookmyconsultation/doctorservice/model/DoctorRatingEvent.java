package com.bookmyconsultation.doctorservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRatingEvent {
    private String id;
    private String doctorId;
    private Integer rating;
}
