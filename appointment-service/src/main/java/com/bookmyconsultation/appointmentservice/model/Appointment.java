package com.bookmyconsultation.appointmentservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Appointment {
    @Id
    @Column(name = "appointment_id")
    private String appointmentId;
    @Column(name = "appointment_date")
    private LocalDate appointmentDate;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "doctor_id")
    private String doctorId;
    @Column(name = "prior_medical_history")
    private String priorMedicalHistory;
    @Column(name = "status")
    private String status;
    @Column(name = "symptoms")
    private String symptoms;
    @Column(name = "time_slot")
    private String timeSlot;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_email_id")
    private String userEmailId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "doctor_name")
    private String doctorName;
    @Column(name = "doctor_email_id")
    private  String doctorEmailId;
}
