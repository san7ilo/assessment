package com.riwi.assessment.domain.repositories;


import com.riwi.assessment.domain.entities.Users;
import com.riwi.assessment.utils.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByDocumentNumber(String documentNumber);

    List<Users> findByRole(RoleEnum role);
}

