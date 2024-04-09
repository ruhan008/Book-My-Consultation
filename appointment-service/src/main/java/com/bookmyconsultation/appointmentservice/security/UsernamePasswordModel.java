package com.bookmyconsultation.appointmentservice.security;

import lombok.Data;

@Data
public class UsernamePasswordModel {
    private String username;
    private String password;
}
