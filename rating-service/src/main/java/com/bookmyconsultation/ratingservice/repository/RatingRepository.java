package com.bookmyconsultation.ratingservice.repository;

import com.bookmyconsultation.ratingservice.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RatingRepository extends MongoRepository<Rating,String> {
}
