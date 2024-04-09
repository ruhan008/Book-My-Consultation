package com.bookmyconsultation.appointmentservice.service;

import com.bookmyconsultation.appointmentservice.model.Appointment;
import com.bookmyconsultation.appointmentservice.model.DoctorAvailabilityMap;
import com.bookmyconsultation.appointmentservice.model.Prescription;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface AppointmentService {

    void updateDoctorAvailabilityDetails(String doctorId, DoctorAvailabilityMap doctorAvailabilityMap);

    DoctorAvailabilityMap getDoctorAvailabilityDetails(String doctorId);

    String bookAppointmentForUser(Appointment appointment, String authToken) throws JsonProcessingException;

    Appointment getAppointmentDetails(String appointmentId);

    List<Appointment> getAppointmentsListForUser(String userId);

    void uploadPrescriptionsForUser(Prescription prescription) throws JsonProcessingException;

    Integer updatePaymentStatus(String appointmentId);
}
