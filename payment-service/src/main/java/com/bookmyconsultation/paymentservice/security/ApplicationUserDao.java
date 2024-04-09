package com.bookmyconsultation.paymentservice.security;

public interface ApplicationUserDao {
    public ApplicationUser loadUserByUsername(String s);
}
