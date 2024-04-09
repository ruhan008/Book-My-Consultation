package com.bookmyconsultation.notificationservice.constants;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationConstants {
    public static final List<String> DOCTOR_EVENTS = List.of("DoctorRegistration","DoctorApprovalEvent","DoctorRejectionEvent");
    public static final List<String> APPOINTMENT_EVENTS = List.of("UserAppointmentEvent","PrescriptionUploadEvent");
    public static final List<String> PAYMENT_EVENTS = List.of("AppointmentPaymentEvent");
    public static final List<String> USER_EVENTS = List.of("UserRegistrationEvent");
}
