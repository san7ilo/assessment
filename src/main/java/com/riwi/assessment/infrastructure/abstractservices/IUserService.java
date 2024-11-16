package com.riwi.assessment.infrastructure.abstractservices;


import com.riwi.assessment.application.dtos.request.UserRequest;
import com.riwi.assessment.application.dtos.response.UserResponse;
import com.riwi.assessment.utils.enums.RoleEnum;

import java.util.List;

public interface IUserService extends CrudAbstractService<UserRequest, UserResponse, Long> {

    UserResponse getByDocumentNumber(String documentNumber);

    List<UserResponse> getByRole(RoleEnum role);

}
