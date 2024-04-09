package com.bookmyconsultation.appointmentservice.repository;

import com.bookmyconsultation.appointmentservice.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    Long deleteByDoctorId(String doctorId);

    List<Availability> findByDoctorIdAndIsBooked(String doctorId, boolean isBooked);

    @Modifying
    @Query(value = "update Availability set is_booked = true where doctor_id = :doctorId and availability_date =" +
            " :appointmentDate and time_slot = :timeSlot", nativeQuery = true)
    void updateBookedSlotStatus(String doctorId, LocalDate appointmentDate, String timeSlot);
}
