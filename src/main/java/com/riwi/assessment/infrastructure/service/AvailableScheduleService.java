package com.riwi.assessment.infrastructure.service;


import com.riwi.assessment.application.dtos.request.AvailableScheduleRequest;
import com.riwi.assessment.application.dtos.response.AvailableScheduleResponse;
import com.riwi.assessment.domain.entities.AvailableSchedule;
import com.riwi.assessment.domain.entities.Users;
import com.riwi.assessment.domain.repositories.AvailableScheduleRepository;
import com.riwi.assessment.domain.repositories.UsersRepository;
import com.riwi.assessment.infrastructure.abstractservices.IAvailableScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailableScheduleService implements IAvailableScheduleService {

    @Autowired
    private AvailableScheduleRepository availableScheduleRepository;

    @Autowired
    private UsersRepository usersRepository; // Repositorio para obtener el doctor

    @Override
    public AvailableScheduleResponse create(AvailableScheduleRequest request) {
        // Verificar disponibilidad
        boolean exists = availableScheduleRepository.existsByDoctorIdAndDateAndStartTimeBetween(
                request.getDoctorId(), request.getDate(), request.getStartTime(), request.getEndTime()
        );

        if (exists) {
            throw new IllegalArgumentException("El médico ya tiene disponibilidad en este horario.");
        }

        // Obtener el objeto Users (doctor) correspondiente al doctorId
        Users doctor = usersRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));

        // Crear la nueva disponibilidad
        AvailableSchedule availableSchedule = new AvailableSchedule(
                request.getDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.isAvailable(),
                doctor
        );
        availableSchedule = availableScheduleRepository.save(availableSchedule);

        return new AvailableScheduleResponse(
                availableSchedule.getId(),
                availableSchedule.getDate(),
                availableSchedule.getStartTime(),
                availableSchedule.getEndTime(),
                availableSchedule.getDoctor().getName()
        );
    }

    @Override
    public AvailableScheduleResponse update(AvailableScheduleRequest request, Long id) {
        AvailableSchedule existingSchedule = availableScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada"));

        // Obtener el objeto Users (doctor) correspondiente al doctorId
        Users doctor = usersRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));

        // Actualizar los valores
        existingSchedule.setDate(request.getDate());
        existingSchedule.setStartTime(request.getStartTime());
        existingSchedule.setEndTime(request.getEndTime());
        existingSchedule.setAvailable(request.isAvailable());
        existingSchedule.setDoctor(doctor); // Actualizamos el doctor

        availableScheduleRepository.save(existingSchedule);

        return new AvailableScheduleResponse(
                existingSchedule.getId(),
                existingSchedule.getDate(),
                existingSchedule.getStartTime(),
                existingSchedule.getEndTime(),
                existingSchedule.getDoctor().getName()
        );
    }

    @Override
    public AvailableScheduleResponse getById(Long id) {
        AvailableSchedule availableSchedule = availableScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada"));

        return new AvailableScheduleResponse(
                availableSchedule.getId(),
                availableSchedule.getDate(),
                availableSchedule.getStartTime(),
                availableSchedule.getEndTime(),
                availableSchedule.getDoctor().getName()
        );
    }

    @Override
    public List<AvailableScheduleResponse> getByDate(LocalDate date) {
        List<AvailableSchedule> schedules = availableScheduleRepository.findByDate(date);

        if (schedules.isEmpty()) {
            throw new IllegalArgumentException("No hay horarios disponibles para esta fecha.");
        }

        return schedules.stream()
                .map(schedule -> new AvailableScheduleResponse(
                        schedule.getId(),
                        schedule.getDate(),
                        schedule.getStartTime(),
                        schedule.getEndTime(),
                        schedule.getDoctor().getName()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public void delete(Long id) {
        availableScheduleRepository.deleteById(id);
    }

    @Override
    public AvailableScheduleResponse getByDoctorId(Long doctorId) {
        List<AvailableSchedule> schedules = availableScheduleRepository.findByDoctorIdAndDateBetween(
                doctorId, LocalDate.now().minusDays(30), LocalDate.now().plusDays(30)
        );

        if (schedules.isEmpty()) {
            throw new IllegalArgumentException("No hay disponibilidad para este médico.");
        }

        // Devolver el primer horario disponible
        AvailableSchedule firstAvailable = schedules.get(0);
        return new AvailableScheduleResponse(
                firstAvailable.getId(),
                firstAvailable.getDate(),
                firstAvailable.getStartTime(),
                firstAvailable.getEndTime(),
                firstAvailable.getDoctor().getName()
        );
    }

    @Override
    public Page<AvailableScheduleResponse> getAll(int page, int size) {
        Page<AvailableSchedule> availableSchedules = availableScheduleRepository.findAll(PageRequest.of(page, size));
        return availableSchedules.map(availableSchedule -> new AvailableScheduleResponse(
                availableSchedule.getId(),
                availableSchedule.getDate(),
                availableSchedule.getStartTime(),
                availableSchedule.getEndTime(),
                availableSchedule.getDoctor().getName()
        ));
    }
}


