package com.bookmyconsultation.ratingservice.service;

import com.bookmyconsultation.ratingservice.model.Rating;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface RatingService {
    void processRatingReceived(Rating rating) throws JsonProcessingException;
}
