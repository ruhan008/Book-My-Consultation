package com.bookmyconsultation.appointmentservice.controller;

import com.bookmyconsultation.appointmentservice.client.DoctorServiceClient;
import com.bookmyconsultation.appointmentservice.model.Appointment;
import com.bookmyconsultation.appointmentservice.model.DoctorAvailabilityMap;
import com.bookmyconsultation.appointmentservice.model.Prescription;
import com.bookmyconsultation.appointmentservice.service.AppointmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorServiceClient doctorServiceClient;

    @PostMapping("/doctor/{doctorId}/availability")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> updateDoctorAvailabilityDetails(@PathVariable String doctorId,
                                                                @RequestBody DoctorAvailabilityMap availabilityMap) {
        appointmentService.updateDoctorAvailabilityDetails(doctorId, availabilityMap);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/doctor/{doctorId}/availability")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<DoctorAvailabilityMap> getDoctorAvailabilityDetails (@PathVariable(name = "doctorId") String
                                                                               doctorId) {
        return new ResponseEntity<>(appointmentService.getDoctorAvailabilityDetails(doctorId), HttpStatus.OK);
    }

    @PostMapping("/appointments")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> bookAppointmentForUser (@RequestHeader(name = "Authorization") String authToken,
                                                          @RequestBody Appointment appointment)
            throws JsonProcessingException {
        return new ResponseEntity<>(appointmentService.bookAppointmentForUser(appointment, authToken),HttpStatus.OK);
    }

    @GetMapping("/appointments/{appointmentId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Appointment> getAppointmentDetails (@PathVariable String appointmentId) {
        return new ResponseEntity<>(appointmentService.getAppointmentDetails(appointmentId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/appointments")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Appointment>> getAPpointmentsListForUser (@PathVariable String userId) {
        return new ResponseEntity<>(appointmentService.getAppointmentsListForUser(userId), HttpStatus.OK);
    }

    @PostMapping("/prescriptions")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Void> uploadPrescriptionsForUserAppointment (@RequestBody Prescription prescription)
            throws JsonProcessingException {
        appointmentService.uploadPrescriptionsForUser(prescription);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/appointments/{appointmentId}/payment")
    @PreAuthorize("hasAnyRole('USER')")
    public Integer updatePaymentStatus (@PathVariable(name = "appointmentId") String appointmentId) {
        return appointmentService.updatePaymentStatus(appointmentId);
    }

}
