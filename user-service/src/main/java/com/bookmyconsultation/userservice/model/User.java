package com.bookmyconsultation.userservice.model;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "User")
public class User {
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
    @NotNull(message = "Date of Birth field cant be null")
    @Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))", message = "Please enter date in format - YYYY-MM-DD")
    private String dob;
    @NotNull(message = "Email field cant be null")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Please enter a valid email id")
    private String emailId;
    @NotNull(message = "Mobile field cant be null")
    @Pattern(regexp = "^[0-9]{10}", message = "Mobile number is invalid")
    private String mobile;
    private LocalDate createdDate;
}
