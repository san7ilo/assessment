package com.riwi.assessment.application.dtos.response;


import com.riwi.assessment.utils.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAppointmentHistory {

    private Long appointmentId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private String notes;
    private AppointmentStatus status;
    private String doctorName;
}
