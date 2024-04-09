package com.bookmyconsultation.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor {
    private String id;
    private String firstName;
    private String lastName;
    private String speciality;
    private String dob;
    private String mobile;
    private String emailId;
    private String pan;
    private String status;
    private String approvedBy;
    private String approverComments;
    private LocalDate registrationDate;
    private LocalDate verificationDate;
}
