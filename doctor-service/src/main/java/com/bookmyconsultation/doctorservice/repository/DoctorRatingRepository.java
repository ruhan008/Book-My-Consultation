package com.bookmyconsultation.doctorservice.repository;

import com.bookmyconsultation.doctorservice.model.DoctorRating;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorRatingRepository extends MongoRepository<DoctorRating,String> {
}
