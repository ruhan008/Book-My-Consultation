package com.bookmyconsultation.doctorservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Document(collection = "Doctor")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Doctor {
    @Id
    private String id;
    @Size(max = 20,min = 1, message = "First Name field can have max of 20 characters")
    @NotNull(message = "First Name field cant be null")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Please enter a valid name")
    private String firstName;
    @Size(max = 20,min = 1, message = "Last Name field can have max of 20 characters")
    @NotNull(message = "Last Name field cant be null")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Please enter a valid name")
    private String lastName;
    private String speciality;
    @NotNull(message = "DOB field cant be null")
    @Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))", message = "Please enter date in format - YYYY-MM-DD")
    private String dob;
    @NotNull(message = "Mobile field cant be null")
    @Pattern(regexp = "^[0-9]{10}", message = "Mobile number is invalid")
    private String mobile;
    @NotNull(message = "Email field cant be null")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Please enter a valid email id")
    private String emailId;
    @NotNull(message = "Pan field cant be null")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{10}", message = "Pan card should be a mix of 10 alphanumeric characters")
    private String pan;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String approvedBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String approverComments;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate registrationDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate verificationDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double averageDoctorRating;
}
