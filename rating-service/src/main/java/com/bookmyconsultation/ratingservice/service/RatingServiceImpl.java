package com.bookmyconsultation.ratingservice.service;

import com.bookmyconsultation.ratingservice.model.DoctorRating;
import com.bookmyconsultation.ratingservice.model.Rating;
import com.bookmyconsultation.ratingservice.producer.KafkaProducer;
import com.bookmyconsultation.ratingservice.repository.DoctorRatingRepository;
import com.bookmyconsultation.ratingservice.repository.RatingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bookmyconsultation.ratingservice.constant.RatingConstants.DOCTOR_RATING_EVENT;

@Service
public class RatingServiceImpl implements RatingService{

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private DoctorRatingRepository doctorRatingRepository;

    @Override
    public void processRatingReceived(Rating rating) throws JsonProcessingException {
        //Save the rating received in the rating repository first
        ratingRepository.save(rating);
        //Check if there is a prior rating for the same doctor in DoctorRating repository
        Optional<DoctorRating> doctorRatingOptional = doctorRatingRepository.findById(rating.getDoctorId());
        DoctorRating doctorRating;
        /* If present then pass the doctorRating object received to updateDoctorRating method to
           update the individual ratings count and average doctor rating */
        if (doctorRatingOptional.isPresent()) {
            doctorRating = doctorRatingOptional.get();
            updateDoctorRating(doctorRating, rating.getRating());
            doctorRatingRepository.save(doctorRating);
        } else {
            // If first rating event then instantiate with default counts
            doctorRating = DoctorRating.builder()
                    .doctorId(rating.getDoctorId())
                    .oneStarRatingsCount(0)
                    .twoStarRatingsCount(0)
                    .threeStarRatingsCount(0)
                    .fourStarRatingsCount(0)
                    .fiveStarRatingsCount(0)
                    .build();
            updateDoctorRating(doctorRating, rating.getRating());
            doctorRatingRepository.save(doctorRating);
        }
        kafkaProducer.sendRatingEvent(doctorRating, DOCTOR_RATING_EVENT);
    }

    private void updateDoctorRating(DoctorRating doctorRating, Integer newRating) {
        //Update the rating count based on rating received
        switch (newRating) {
            case 1:
                Integer currentOneStarCount = doctorRating.getOneStarRatingsCount();
                doctorRating.setOneStarRatingsCount(currentOneStarCount+1);
                break;
            case 2:
                Integer currentTwoStarCount = doctorRating.getTwoStarRatingsCount();
                doctorRating.setTwoStarRatingsCount(currentTwoStarCount+1);
                break;
            case 3:
                Integer currentThreeStarCount = doctorRating.getThreeStarRatingsCount();
                doctorRating.setThreeStarRatingsCount(currentThreeStarCount+1);
                break;
            case 4:
                Integer currentFourStarCount = doctorRating.getFourStarRatingsCount();
                doctorRating.setFourStarRatingsCount(currentFourStarCount+1);
                break;
            case 5:
                Integer currentFiveStarCount = doctorRating.getFiveStarRatingsCount();
                doctorRating.setFiveStarRatingsCount(currentFiveStarCount+1);
                break;
            default:
                break;
        }
        /* Calculate the average rating for the doctor based on the sum of ratings received divided by total number of
           ratings received and then use apache commons library to round off the two places after decimal point */
        Double avgRating = Precision.round((((double)(doctorRating.getOneStarRatingsCount()) + (doctorRating.getTwoStarRatingsCount() * 2) +
                (doctorRating.getThreeStarRatingsCount() * 3) + (doctorRating.getFourStarRatingsCount() * 4) +
                (doctorRating.getFiveStarRatingsCount() * 5)) / (doctorRating.getOneStarRatingsCount() +
                doctorRating.getTwoStarRatingsCount() + doctorRating.getThreeStarRatingsCount() +
                doctorRating.getFourStarRatingsCount() + doctorRating.getFiveStarRatingsCount())),2);
        doctorRating.setAverageDoctorRating(avgRating);
    }
    
}
