package com.bookmyconsultation.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Payment")
public class Payment {
    @Id
    private String id;
    private String appointmentId;
    private LocalDateTime createdDate;
    private String userName;
    private String userEmailId;
}
