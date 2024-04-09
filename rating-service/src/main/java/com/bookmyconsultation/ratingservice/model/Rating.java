package com.bookmyconsultation.ratingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Rating")
public class Rating {
    @Id
    private String id;
    @NotNull(message = "Doctor Id field cannot be null")
    private String doctorId;
    @Min(value = 1, message = "Please enter a value between 1-5 (inclusive of 1 and 5")
    @Max(value = 5, message = "Please enter a value between 1-5 (inclusive of 1 and 5")
    private Integer rating;
}
