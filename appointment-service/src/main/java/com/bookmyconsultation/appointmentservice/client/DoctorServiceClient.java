package com.bookmyconsultation.appointmentservice.client;

import com.bookmyconsultation.appointmentservice.model.request.DoctorDetailsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "doctor-service", url = "http://${DOCTOR_SERVICE_HOST:localhost}:8081")
public interface DoctorServiceClient {
    @GetMapping("/doctor/{doctorId}/details")
    DoctorDetailsRequest getDoctorDetailsRequest (@PathVariable(name = "doctorId") String doctorId,
                                                  @RequestHeader(name = "Authorization") String authToken);

}
