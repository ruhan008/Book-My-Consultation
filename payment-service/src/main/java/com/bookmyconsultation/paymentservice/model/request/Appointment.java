package com.bookmyconsultation.paymentservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    private String appointmentId;
    private LocalDate appointmentDate;
    private LocalDateTime createdDate;
    private String doctorId;
    private String priorMedicalHistory;
    private String status;
    private String symptoms;
    private String timeSlot;
    private String userId;
    private String userEmailId;
    private String userName;
    private String doctorName;
    private  String doctorEmailId;
}