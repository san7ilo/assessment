package com.riwi.assessment.application.controllers;


import com.riwi.assessment.application.dtos.request.AppointmentRequest;
import com.riwi.assessment.application.dtos.response.PatientAppointmentHistory;
import com.riwi.assessment.application.dtos.response.PatientAppointmentList;
import com.riwi.assessment.infrastructure.service.PatientAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Management of patient appointments")
public class AppointmentController {

    private final PatientAppointmentService appointmentService;

    public AppointmentController(PatientAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(
            summary = "Create a new appointment",
            description = "Schedules a new appointment for a patient with a doctor."
    )
    @ApiResponse(responseCode = "200", description = "Appointment created successfully")
    @PostMapping
    public ResponseEntity<PatientAppointmentList> createAppointment(
            @Validated @RequestBody AppointmentRequest request) {
        PatientAppointmentList response = appointmentService.create(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get all appointments (paginated)",
            description = "Retrieves a paginated list of all appointments."
    )
    @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<PatientAppointmentList>> getAllAppointments(
            @Parameter(description = "Page number for pagination", example = "0", required = true)
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10", required = true)
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<PatientAppointmentList> response = appointmentService.getAll(page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get an appointment by ID",
            description = "Fetches the details of a specific appointment by its ID."
    )
    @ApiResponse(responseCode = "200", description = "Appointment retrieved successfully")
    @GetMapping("/{id}")
    public ResponseEntity<PatientAppointmentList> getAppointmentById(
            @Parameter(description = "ID of the appointment to retrieve", required = true)
            @PathVariable Long id) {
        PatientAppointmentList response = appointmentService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete an appointment",
            description = "Deletes an existing appointment by its ID."
    )
    @ApiResponse(responseCode = "204", description = "Appointment deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @Parameter(description = "ID of the appointment to delete", required = true)
            @PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all appointments for a patient",
            description = "Fetches the list of all appointments associated with a specific patient."
    )
    @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PatientAppointmentList> getAppointmentsByPatient(
            @Parameter(description = "ID of the patient", required = true)
            @PathVariable Long patientId) {
        PatientAppointmentList response = appointmentService.getByPatientId(patientId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get appointments by doctor and date",
            description = "Fetches all appointments scheduled with a specific doctor on a given date."
    )
    @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully")
    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<List<PatientAppointmentHistory>> getAppointmentsByDoctorAndDate(
            @Parameter(description = "ID of the doctor", required = true)
            @PathVariable Long doctorId,
            @Parameter(description = "Date to filter appointments by (YYYY-MM-DD)", required = true)
            @PathVariable LocalDate date) {
        List<PatientAppointmentHistory> response = appointmentService.getAppointmentsByDoctorAndDate(doctorId, date);
        return ResponseEntity.ok(response);
    }
}

