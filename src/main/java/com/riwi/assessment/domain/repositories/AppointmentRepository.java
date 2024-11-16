package com.riwi.assessment.domain.repositories;


import com.riwi.assessment.domain.entities.Appointment;
import com.riwi.assessment.utils.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date);

    boolean existsByDoctorIdAndDateAndStartTimeBetween(Long doctorId, LocalDate date, LocalTime start, LocalTime end);
}

