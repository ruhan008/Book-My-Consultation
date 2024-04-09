package com.bookmyconsultation.appointmentservice.repository;

import com.bookmyconsultation.appointmentservice.model.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
}
