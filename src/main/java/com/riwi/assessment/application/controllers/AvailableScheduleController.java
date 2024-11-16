package com.riwi.assessment.application.controllers;


import com.riwi.assessment.application.dtos.request.AvailableScheduleRequest;
import com.riwi.assessment.application.dtos.response.AvailableScheduleResponse;
import com.riwi.assessment.infrastructure.service.AvailableScheduleService;
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
@RequestMapping("/api/available-schedules")
@Tag(name = "Available Schedules", description = "Management of doctors' availability schedules")
public class AvailableScheduleController {

    private final AvailableScheduleService availableScheduleService;

    public AvailableScheduleController(AvailableScheduleService availableScheduleService) {
        this.availableScheduleService = availableScheduleService;
    }

    @Operation(
            summary = "Create a new available schedule",
            description = "Registers a new availability schedule for a doctor."
    )
    @ApiResponse(responseCode = "200", description = "Schedule created successfully")
    @PostMapping
    public ResponseEntity<AvailableScheduleResponse> createAvailableSchedule(
            @Validated @RequestBody AvailableScheduleRequest request) {
        AvailableScheduleResponse response = availableScheduleService.create(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update an existing schedule",
            description = "Updates the details of an existing availability schedule by its ID."
    )
    @ApiResponse(responseCode = "200", description = "Schedule updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<AvailableScheduleResponse> updateAvailableSchedule(
            @Parameter(description = "ID of the schedule to update", required = true)
            @PathVariable Long id,
            @Validated @RequestBody AvailableScheduleRequest request) {
        AvailableScheduleResponse response = availableScheduleService.update(request, id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a schedule",
            description = "Deletes an existing availability schedule by its ID."
    )
    @ApiResponse(responseCode = "204", description = "Schedule deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailableSchedule(
            @Parameter(description = "ID of the schedule to delete", required = true)
            @PathVariable Long id) {
        availableScheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get a schedule by ID",
            description = "Fetches the details of a specific availability schedule by its ID."
    )
    @ApiResponse(responseCode = "200", description = "Schedule retrieved successfully")
    @GetMapping("/{id}")
    public ResponseEntity<AvailableScheduleResponse> getAvailableScheduleById(
            @Parameter(description = "ID of the schedule to retrieve", required = true)
            @PathVariable Long id) {
        AvailableScheduleResponse response = availableScheduleService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get schedules by date",
            description = "Fetches all available schedules for a specific date."
    )
    @ApiResponse(responseCode = "200", description = "Schedules retrieved successfully")
    @GetMapping("/date/{date}")
    public ResponseEntity<List<AvailableScheduleResponse>> getSchedulesByDate(
            @Parameter(description = "Date to filter schedules by (YYYY-MM-DD)", required = true)
            @PathVariable LocalDate date) {
        List<AvailableScheduleResponse> responses = availableScheduleService.getByDate(date);
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Get schedules by doctor ID",
            description = "Fetches the available schedules of a specific doctor within a 30-day range."
    )
    @ApiResponse(responseCode = "200", description = "Schedules retrieved successfully")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<AvailableScheduleResponse> getSchedulesByDoctorId(
            @Parameter(description = "ID of the doctor", required = true)
            @PathVariable Long doctorId) {
        AvailableScheduleResponse response = availableScheduleService.getByDoctorId(doctorId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "List all schedules",
            description = "Retrieves a paginated list of all availability schedules."
    )
    @ApiResponse(responseCode = "200", description = "Schedules retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<AvailableScheduleResponse>> getAllSchedules(
            @Parameter(description = "Page number for pagination", example = "0", required = true)
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10", required = true)
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<AvailableScheduleResponse> responses = availableScheduleService.getAll(page, size);
        return ResponseEntity.ok(responses);
    }
}

