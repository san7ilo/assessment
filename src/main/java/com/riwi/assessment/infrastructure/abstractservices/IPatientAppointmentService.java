package com.riwi.assessment.infrastructure.abstractservices;



import com.riwi.assessment.application.dtos.request.AppointmentRequest;
import com.riwi.assessment.application.dtos.response.PatientAppointmentHistory;
import com.riwi.assessment.application.dtos.response.PatientAppointmentList;

import java.time.LocalDate;
import java.util.List;

public interface IPatientAppointmentService extends CrudAbstractService<AppointmentRequest, PatientAppointmentList, Long> {

    List<PatientAppointmentHistory> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date);

    PatientAppointmentList getByPatientId(Long patientId);
}


