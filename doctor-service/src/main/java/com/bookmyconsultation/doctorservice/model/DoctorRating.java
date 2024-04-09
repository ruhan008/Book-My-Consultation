package com.bookmyconsultation.doctorservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "DoctorRating")
public class DoctorRating {
    @Id
    private String doctorId;
    private Integer oneStarRatingsCount;
    private Integer twoStarRatingsCount;
    private Integer threeStarRatingsCount;
    private Integer fourStarRatingsCount;
    private Integer fiveStarRatingsCount;
    private Double averageDoctorRating;
}
