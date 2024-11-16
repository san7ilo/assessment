package com.riwi.assessment.application.dtos.response;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAppointmentList {

    private Long patientId;
    private String patientName;
    private List<PatientAppointmentHistory> appointments;
}

