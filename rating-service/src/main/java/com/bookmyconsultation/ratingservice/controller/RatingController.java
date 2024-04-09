package com.bookmyconsultation.ratingservice.controller;

import com.bookmyconsultation.ratingservice.model.Rating;
import com.bookmyconsultation.ratingservice.service.RatingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.bookmyconsultation.ratingservice.constant.RatingConstants.ACCEPTED_YOUR_RATING_MESSAGE;

@RestController
@Slf4j
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/ratings")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<String> acceptDoctorRating(@RequestBody @Valid Rating rating) throws JsonProcessingException {
        ratingService.processRatingReceived(rating);
        return ResponseEntity.ok().body(ACCEPTED_YOUR_RATING_MESSAGE);
    }
}
