package com.bookmyconsultation.doctorservice.repository;

import com.bookmyconsultation.doctorservice.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor,String> {
}
