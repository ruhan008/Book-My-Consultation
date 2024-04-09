package com.bookmyconsultation.appointmentservice.service;

import com.bookmyconsultation.appointmentservice.client.DoctorServiceClient;
import com.bookmyconsultation.appointmentservice.client.UserServiceClient;
import com.bookmyconsultation.appointmentservice.exception.AppointmentIDInvalidException;
import com.bookmyconsultation.appointmentservice.exception.PaymentPendingException;
import com.bookmyconsultation.appointmentservice.model.Appointment;
import com.bookmyconsultation.appointmentservice.model.Availability;
import com.bookmyconsultation.appointmentservice.model.DoctorAvailabilityMap;
import com.bookmyconsultation.appointmentservice.model.Prescription;
import com.bookmyconsultation.appointmentservice.model.request.DoctorDetailsRequest;
import com.bookmyconsultation.appointmentservice.model.request.UserDetailsRequest;
import com.bookmyconsultation.appointmentservice.producer.KafkaProducer;
import com.bookmyconsultation.appointmentservice.repository.AppointmentRepository;
import com.bookmyconsultation.appointmentservice.repository.AvailabilityRepository;
import com.bookmyconsultation.appointmentservice.repository.PrescriptionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bookmyconsultation.appointmentservice.constant.AppointmentConstants.PRESCRIPTION_UPLOAD_EVENT;
import static com.bookmyconsultation.appointmentservice.constant.AppointmentConstants.USER_APPOINTMENT_EVENT;

@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService{

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DoctorServiceClient doctorServiceClient;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public void updateDoctorAvailabilityDetails(String doctorId, DoctorAvailabilityMap doctorAvailabilityMap) {
        // Map the date and time slots received as map to individual entities for availability repository
        List<Availability> doctorAvailabilitySlots = mapDoctorAvailabilityMapToAvailabilityMapEntities
                (doctorId, doctorAvailabilityMap);
        // Deleting all existing slots of doctor to avoid duplicates
        Long deletedSlots = availabilityRepository.deleteByDoctorId(doctorId);
        log.info("Deleted {} pre-existing availability slots for doctor with id:{}", deletedSlots, doctorId);
        availabilityRepository.saveAll(doctorAvailabilitySlots);
    }

    @Override
    public DoctorAvailabilityMap getDoctorAvailabilityDetails(String doctorId) {
        List<Availability> doctorAvailabilitySlots = availabilityRepository.findByDoctorIdAndIsBooked(doctorId,
                false);
        return mapAvailabilityListToDoctorAvailabilityMapModel(doctorAvailabilitySlots, doctorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String bookAppointmentForUser(Appointment appointment, String authToken) throws JsonProcessingException {
        appointment.setAppointmentId(UUID.randomUUID().toString());
        /* Calling the doctor-service and user-service to get user and doctor details related to appointment
           so that we can send email notification to user on appointment details  */
        fetchAndUpdateUserAndDoctorDetails(appointment, authToken);
        appointment.setCreatedDate(LocalDateTime.now());
        appointment.setStatus("PendingPayment");
        // Updating the availability of doctor for the time slot booked
        availabilityRepository.updateBookedSlotStatus(appointment.getDoctorId(),appointment.getAppointmentDate(),
                appointment.getTimeSlot());
        // Saving the appointment in Appointment repository
        Appointment savedAppointment = appointmentRepository.save(appointment);
        String appointmentStringValue = objectMapper.writeValueAsString(savedAppointment);
        // Send appointment event to notification-service
        kafkaProducer.sendEvent(USER_APPOINTMENT_EVENT, appointmentStringValue);
        return savedAppointment.getAppointmentId();
    }

    @Override
    public Appointment getAppointmentDetails(String appointmentId) {
        Optional<Appointment> validAppointment = appointmentRepository.findById(appointmentId);
        if (validAppointment.isPresent()){
            return validAppointment.get();
        }
        throw new AppointmentIDInvalidException("Appointment not found");
    }

    @Override
    public List<Appointment> getAppointmentsListForUser(String userId) {
        return appointmentRepository.findByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadPrescriptionsForUser(Prescription prescription) throws JsonProcessingException {
        // Check if appointment payment is done, and allow doctor to upload prescription only if its paid
        Appointment appointment = appointmentRepository.findById(prescription.getAppointmentId()).get();
        if (appointment.getStatus().equalsIgnoreCase("Confirmed")) {
            prescription.setDoctorName(appointment.getDoctorName());
            prescription.setUserName(appointment.getUserName());
            prescription.setUserEmailId(appointment.getUserEmailId());
            prescriptionRepository.save(prescription);
            String prescriptionStringValue = objectMapper.writeValueAsString(prescription);
            kafkaProducer.sendEvent(PRESCRIPTION_UPLOAD_EVENT, prescriptionStringValue);
            return;
        }
        // else send error message saying payment pending from user
        throw new PaymentPendingException("Prescription cannot be issued since the payment status is pending");
    }

    @Override
    @Transactional
    public Integer updatePaymentStatus(String appointmentId) {
        return appointmentRepository.updatePaymentStatusForAppointment(appointmentId);
    }

    private void fetchAndUpdateUserAndDoctorDetails(Appointment appointment, String authToken) {
        // Fetching doctor details from doctor-service
        DoctorDetailsRequest doctorDetailsRequest = doctorServiceClient.getDoctorDetailsRequest(appointment.getDoctorId(),
                authToken);
        // Fetching user details from user-service
        UserDetailsRequest userDetailsRequest = userServiceClient.getUserDetailsRequest(appointment.getUserId(),
                authToken);
        appointment.setUserName(userDetailsRequest.getUserName());
        appointment.setUserEmailId(userDetailsRequest.getEmailId());
        appointment.setDoctorName(doctorDetailsRequest.getDoctorName());
        appointment.setDoctorEmailId(doctorDetailsRequest.getDoctorEmailId());
    }

    private DoctorAvailabilityMap mapAvailabilityListToDoctorAvailabilityMapModel(List<Availability>
                                                                                          doctorAvailabilitySlots,
                                                                                  String doctorId) {
        DoctorAvailabilityMap doctorAvailabilityMap = new DoctorAvailabilityMap();
        doctorAvailabilityMap.setDoctorId(doctorId);
        doctorAvailabilityMap.setAvailabilityMap(new HashMap<>());
        doctorAvailabilitySlots.forEach(slot -> {
            LocalDate date = slot.getAvailabilityDate();
            String timeSlot = slot.getTimeSlot();
            if (doctorAvailabilityMap.getAvailabilityMap().containsKey(date)) {
                List<String> timeSlots = doctorAvailabilityMap.getAvailabilityMap().get(date);
                timeSlots.add(timeSlot);
                doctorAvailabilityMap.getAvailabilityMap().put(date, timeSlots);
            } else {
                doctorAvailabilityMap.getAvailabilityMap().put(date, new ArrayList<>(List.of(timeSlot)));
            }
        });
        return doctorAvailabilityMap;
    }

    private List<Availability> mapDoctorAvailabilityMapToAvailabilityMapEntities(String doctorId,
                                                                                 DoctorAvailabilityMap
                                                                                         doctorAvailabilityMap) {
        List<Availability> doctorAvailabilitySlots = new ArrayList<>();
        doctorAvailabilityMap.getAvailabilityMap().forEach((date,timeSlots) -> timeSlots.forEach(slot -> {
            Availability availability = Availability.builder()
                    .doctorId(doctorId)
                    .availabilityDate(date)
                    .isBooked(false)
                    .timeSlot(slot)
                    .build();
            doctorAvailabilitySlots.add(availability);
        }));
        return doctorAvailabilitySlots;
    }
}
