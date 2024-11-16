package com.riwi.assessment.infrastructure.service;

import com.riwi.assessment.application.dtos.request.AppointmentRequest;
import com.riwi.assessment.application.dtos.response.PatientAppointmentHistory;
import com.riwi.assessment.application.dtos.response.PatientAppointmentList;
import com.riwi.assessment.domain.entities.Appointment;
import com.riwi.assessment.domain.entities.Users;
import com.riwi.assessment.domain.repositories.AppointmentRepository;
import com.riwi.assessment.domain.repositories.UsersRepository;
import com.riwi.assessment.infrastructure.abstractservices.IPatientAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientAppointmentService implements IPatientAppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Page<PatientAppointmentList> getAll(int page, int size) {
        return appointmentRepository.findAll(PageRequest.of(page, size))
                .map(appointment -> new PatientAppointmentList(
                        appointment.getPatient().getId(),
                        appointment.getPatient().getName(),
                        appointmentRepository.findByPatientId(appointment.getPatient().getId()).stream()
                                .map(app -> new PatientAppointmentHistory(
                                        app.getId(),
                                        app.getDate(),
                                        app.getStartTime(),
                                        app.getEndTime(),
                                        app.getReason(),
                                        app.getNotes(),
                                        app.getStatus(),
                                        app.getDoctor().getName()
                                ))
                                .collect(Collectors.toList())
                ));
    }


    @Override
    public PatientAppointmentList create(AppointmentRequest request) {
        // Verificar si el horario ya está ocupado
        boolean exists = appointmentRepository.existsByDoctorIdAndDateAndStartTimeBetween(
                request.getDoctorId(), request.getDate(), request.getStartTime(), request.getEndTime()
        );

        if (exists) {
            throw new IllegalArgumentException("El médico ya tiene una cita en este horario.");
        }

        // Obtener el paciente y el doctor por ID
        Users patient = usersRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        Users doctor = usersRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));

        // Crear la cita
        Appointment appointment = Appointment.builder()
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .reason(request.getReason())
                .notes(request.getNotes())
                .patient(patient)  // Asignar el paciente
                .doctor(doctor)    // Asignar el doctor
                .build();
        appointmentRepository.save(appointment);

        return new PatientAppointmentList(
                patient.getId(),
                patient.getName(),
                List.of(new PatientAppointmentHistory(
                        appointment.getId(),
                        appointment.getDate(),
                        appointment.getStartTime(),
                        appointment.getEndTime(),
                        appointment.getReason(),
                        appointment.getNotes(),
                        appointment.getStatus(),
                        appointment.getDoctor().getName()
                ))
        );
    }

    @Override
    public PatientAppointmentList update(AppointmentRequest request, Long aLong) {
        return null;
    }


    @Override
    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public PatientAppointmentList getById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        return new PatientAppointmentList(
                appointment.getPatient().getId(),
                appointment.getPatient().getName(),
                List.of(new PatientAppointmentHistory(
                        appointment.getId(),
                        appointment.getDate(),
                        appointment.getStartTime(),
                        appointment.getEndTime(),
                        appointment.getReason(),
                        appointment.getNotes(),
                        appointment.getStatus(),
                        appointment.getDoctor().getName()
                ))
        );
    }

    @Override
    public List<PatientAppointmentHistory> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(doctorId, date);

        if (appointments.isEmpty()) {
            throw new IllegalArgumentException("No hay citas para este médico en la fecha indicada.");
        }

        return appointments.stream()
                .map(appointment -> new PatientAppointmentHistory(
                        appointment.getId(),
                        appointment.getDate(),
                        appointment.getStartTime(),
                        appointment.getEndTime(),
                        appointment.getReason(),
                        appointment.getNotes(),
                        appointment.getStatus(),
                        appointment.getPatient().getName()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public PatientAppointmentList getByPatientId(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        if (appointments.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron citas para este paciente.");
        }
        return new PatientAppointmentList(
                patientId,
                appointments.get(0).getPatient().getName(),
                appointments.stream()
                        .map(app -> new PatientAppointmentHistory(
                                app.getId(),
                                app.getDate(),
                                app.getStartTime(),
                                app.getEndTime(),
                                app.getReason(),
                                app.getNotes(),
                                app.getStatus(),
                                app.getDoctor().getName()
                        ))
                        .collect(Collectors.toList())
        );
    }

}
