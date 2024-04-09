package com.bookmyconsultation.ratingservice.repository;

import com.bookmyconsultation.ratingservice.model.DoctorRating;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorRatingRepository extends MongoRepository<DoctorRating, String> {
}
