package com.bookmyconsultation.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    private String id;
    private String appointmentId;
    private LocalDateTime createdDate;
    private String userName;
    private String userEmailId;
}
