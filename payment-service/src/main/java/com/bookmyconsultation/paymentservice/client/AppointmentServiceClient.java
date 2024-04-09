package com.bookmyconsultation.paymentservice.client;

import com.bookmyconsultation.paymentservice.model.request.Appointment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "appointment-service", url = "http://${APPOINTMENT_SERVICE_HOST:localhost}:8082")
public interface AppointmentServiceClient {

    @GetMapping("/appointments/{appointmentId}")
    ResponseEntity<Appointment> getAppointmentDetails (@PathVariable String appointmentId,
                                                       @RequestHeader(name = "Authorization") String authToken);

    @PostMapping("/appointments/{appointmentId}/payment")
    Integer updatePaymentStatus (@PathVariable(name = "appointmentId") String appointmentId,
                                 @RequestHeader(name = "Authorization") String authToken);

}
