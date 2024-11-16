package com.riwi.assessment.domain.repositories;


import com.riwi.assessment.domain.entities.AvailableSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AvailableScheduleRepository extends JpaRepository<AvailableSchedule, Long> {

    List<AvailableSchedule> findByDoctorIdAndDateBetween(Long doctorId, LocalDate startDate, LocalDate endDate);

    List<AvailableSchedule> findByDate(LocalDate date);

    boolean existsByDoctorIdAndDateAndStartTimeBetween(Long doctorId, LocalDate date, java.time.LocalTime start, java.time.LocalTime end);
}

