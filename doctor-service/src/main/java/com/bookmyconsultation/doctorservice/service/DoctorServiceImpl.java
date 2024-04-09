package com.bookmyconsultation.doctorservice.service;

import com.bookmyconsultation.doctorservice.exception.InvalidDoctorIdException;
import com.bookmyconsultation.doctorservice.model.Doctor;
import com.bookmyconsultation.doctorservice.model.DoctorRating;
import com.bookmyconsultation.doctorservice.model.DoctorRatingEvent;
import com.bookmyconsultation.doctorservice.model.request.DoctorDetailsRequest;
import com.bookmyconsultation.doctorservice.producer.KafkaProducer;
import com.bookmyconsultation.doctorservice.repository.DoctorRatingRepository;
import com.bookmyconsultation.doctorservice.repository.DoctorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.bookmyconsultation.doctorservice.constant.DoctorConstants.DOCTOR_APPROVAL_EVENT;
import static com.bookmyconsultation.doctorservice.constant.DoctorConstants.DOCTOR_REGISTRATION_EVENT;
import static com.bookmyconsultation.doctorservice.constant.DoctorConstants.DOCTOR_REJECTION_EVENT;

@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorRatingRepository doctorRatingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Doctor registerDoctor(Doctor doctor) throws IOException, TemplateException, MessagingException {
        Doctor processedDoctorEntryDetails = processDoctorEntryDetails(doctor);
        Doctor savedDoctor = doctorRepository.save(processedDoctorEntryDetails);
        kafkaProducer.sendDoctorEvent(savedDoctor, DOCTOR_REGISTRATION_EVENT);
        emailService.verifyEmail(processedDoctorEntryDetails.getEmailId());
        return savedDoctor;
    }

    private Doctor processDoctorEntryDetails(Doctor doctor) {
        if (doctor.getSpeciality() == null) {
            doctor.setSpeciality("GENERAL_PHYSICIAN");
        }
        doctor.setStatus("Pending");
        doctor.setRegistrationDate(LocalDate.now());
        return doctor;
    }

    @Override
    public Doctor approveDoctorsRegistration(String doctorId, Doctor doctor) throws JsonProcessingException {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isPresent()) {
            Doctor approvedDoctor = optionalDoctor.get();
            approvedDoctor.setApprovedBy(doctor.getApprovedBy());
            approvedDoctor.setApproverComments(doctor.getApproverComments());
            approvedDoctor.setStatus("Active");
            approvedDoctor.setVerificationDate(LocalDate.now());
            kafkaProducer.sendDoctorEvent(approvedDoctor, DOCTOR_APPROVAL_EVENT);
            return doctorRepository.save(approvedDoctor);
        } else {
            throw new InvalidDoctorIdException("Requested resource is not available");
        }
    }

    @Override
    public Doctor rejectDoctorsRegistration(String doctorId, Doctor doctor) throws JsonProcessingException {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isPresent()) {
            Doctor rejectedDoctor = optionalDoctor.get();
            rejectedDoctor.setApprovedBy(doctor.getApprovedBy());
            rejectedDoctor.setApproverComments(doctor.getApproverComments());
            rejectedDoctor.setStatus("Rejected");
            rejectedDoctor.setVerificationDate(LocalDate.now());
            kafkaProducer.sendDoctorEvent(rejectedDoctor, DOCTOR_REJECTION_EVENT);
            return doctorRepository.save(rejectedDoctor);
        } else {
            throw new InvalidDoctorIdException("Requested resource is not available");
        }
    }

    @Override
    public void processDoctorRatingReceived(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        DoctorRating ratingEvent = objectMapper.readValue(consumerRecord.value(), DoctorRating.class);
        Optional<Doctor> doctorOptional = doctorRepository.findById(ratingEvent.getDoctorId());
        if (doctorOptional.isPresent()) {
            Doctor doctorRatingUpdated = doctorOptional.get();
            doctorRatingUpdated.setAverageDoctorRating(ratingEvent.getAverageDoctorRating());
            doctorRepository.save(doctorRatingUpdated);
        } else {
            log.error("Rating received for invalid doctor id:{}", ratingEvent.getDoctorId());
        }
    }

    @Override
    public Doctor getDoctorDetails(String doctorId) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            return doctor.get();
        }
        throw new InvalidDoctorIdException("Requested resource is not available");
    }

    @Override
    public List<Doctor> getDoctorsBasedOnFilterCriteria(String status, String speciality) {
        Query dynamicQuery = new Query();
        Criteria queryCriteria = new Criteria();
        if (status != null) {
            queryCriteria = Criteria.where("status").is(status);
        }
        if (speciality != null) {
            queryCriteria.and("speciality").is(speciality);
        }
        dynamicQuery.addCriteria(queryCriteria);
        dynamicQuery.with(Sort.by(Sort.Direction.DESC, "averageDoctorRating")).limit(20);
        return mongoTemplate.find(dynamicQuery, Doctor.class,
                "Doctor");
    }

    @Override
    public DoctorDetailsRequest getDoctorDetailsRequest(String doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).get();
        return DoctorDetailsRequest.builder()
                .doctorId(doctorId)
                .doctorName(doctor.getFirstName() + " " + doctor.getLastName())
                .doctorEmailId(doctor.getEmailId())
                .build();
    }

}
