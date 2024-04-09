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
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String dob;
    private String emailId;
    private String mobile;
    private LocalDate createdDate;
}
