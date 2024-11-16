package com.riwi.assessment.infrastructure.abstractservices;


import com.riwi.assessment.application.dtos.request.AvailableScheduleRequest;
import com.riwi.assessment.application.dtos.response.AvailableScheduleResponse;

import java.time.LocalDate;
import java.util.List;

public interface IAvailableScheduleService extends CrudAbstractService<AvailableScheduleRequest, AvailableScheduleResponse, Long> {

    List<AvailableScheduleResponse> getByDate(LocalDate date);

    AvailableScheduleResponse getByDoctorId(Long doctorId);
}

