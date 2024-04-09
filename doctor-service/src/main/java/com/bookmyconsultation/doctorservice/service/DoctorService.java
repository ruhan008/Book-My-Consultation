package com.bookmyconsultation.doctorservice.service;

import com.bookmyconsultation.doctorservice.model.Doctor;
import com.bookmyconsultation.doctorservice.model.request.DoctorDetailsRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import freemarker.template.TemplateException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface DoctorService {

    Doctor registerDoctor(Doctor doctor) throws IOException, TemplateException, MessagingException;

    Doctor approveDoctorsRegistration(String doctorId, Doctor doctor) throws JsonProcessingException;

    Doctor rejectDoctorsRegistration(String doctorId, Doctor doctor) throws JsonProcessingException;

    void processDoctorRatingReceived(ConsumerRecord<String,String> consumerRecord) throws JsonProcessingException;

    Doctor getDoctorDetails(String doctorId);

    List<Doctor> getDoctorsBasedOnFilterCriteria(String status, String speciality);

    DoctorDetailsRequest getDoctorDetailsRequest(String doctorId);

}
