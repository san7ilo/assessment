package com.riwi.assessment.application.dtos.response;

import com.riwi.assessment.utils.enums.RoleEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String documentNumber; // Número de documento
    private RoleEnum role;
    private boolean enabled;

}
