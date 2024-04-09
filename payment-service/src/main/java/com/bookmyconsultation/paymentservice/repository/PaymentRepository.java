package com.bookmyconsultation.paymentservice.repository;

import com.bookmyconsultation.paymentservice.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
}
