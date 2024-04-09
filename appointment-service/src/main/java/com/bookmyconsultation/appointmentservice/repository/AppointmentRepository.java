package com.bookmyconsultation.appointmentservice.repository;

import com.bookmyconsultation.appointmentservice.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    List<Appointment> findByUserId(String userId);

    @Modifying
    @Query(value = "Update Appointment set status = 'Confirmed' where appointment_id = :appointmentId", nativeQuery = true)
    Integer updatePaymentStatusForAppointment (String appointmentId);
}
