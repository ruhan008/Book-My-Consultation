package com.bookmyconsultation.paymentservice.security;

import lombok.Data;

@Data
public class UsernamePasswordModel {
    private String username;
    private String password;
}
