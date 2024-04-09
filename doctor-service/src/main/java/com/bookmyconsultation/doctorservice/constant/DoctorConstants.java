package com.bookmyconsultation.doctorservice.constant;

import org.springframework.stereotype.Component;

@Component
public class DoctorConstants {
    public static final String DOCTOR_REGISTRATION_EVENT = "DoctorRegistration";
    public static final String DOCTOR_APPROVAL_EVENT = "DoctorApprovalEvent";
    public static final String DOCTOR_REJECTION_EVENT = "DoctorRejectionEvent";
}
