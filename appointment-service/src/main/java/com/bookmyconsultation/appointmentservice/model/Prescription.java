package com.bookmyconsultation.appointmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Prescription")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {
    private String id;
    private String userId;
    private String doctorId;
    private String doctorName;
    private String userName;
    private String userEmailId;
    private String appointmentId;
    private String diagnosis;
    private List<Medicine> medicineList;
}
