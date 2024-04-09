package com.bookmyconsultation.appointmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "availability_date")
    private LocalDate availabilityDate;
    @Column(name = "doctor_id")
    private String doctorId;
    @Column(name = "is_booked")
    private Boolean isBooked;
    @Column(name = "time_slot")
    private String timeSlot;
}
