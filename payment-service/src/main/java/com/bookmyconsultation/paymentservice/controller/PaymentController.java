package com.bookmyconsultation.paymentservice.controller;

import com.bookmyconsultation.paymentservice.model.Payment;
import com.bookmyconsultation.paymentservice.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payments")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Payment> acceptPaymentForAppointment (@RequestHeader(name = "Authorization") String authToken,
                                                                @RequestParam(name = "appointmentId")
                                                                            String appointmentId)
            throws JsonProcessingException {
        return new ResponseEntity<>(paymentService.acceptPaymentForAppointment(appointmentId, authToken), HttpStatus.OK);
    }

}
