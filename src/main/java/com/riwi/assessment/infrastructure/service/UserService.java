package com.riwi.assessment.infrastructure.service;

import com.riwi.assessment.application.dtos.request.UserRequest;
import com.riwi.assessment.application.dtos.response.UserResponse;
import com.riwi.assessment.domain.entities.Users;
import com.riwi.assessment.domain.repositories.UsersRepository;
import com.riwi.assessment.infrastructure.abstractservices.IUserService;
import com.riwi.assessment.utils.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @Autowired
    private UsersRepository userRepository;

    // Método para obtener todos los usuarios con paginación
    @Override
    public Page<UserResponse> getAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size))
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getDocumentNumber(),
                        user.getRole(),
                        user.isEnabled() // Se incluye 'enabled' también
                ));
    }

    // Método para crear un nuevo usuario
    @Override
    public UserResponse create(UserRequest request) {
        Users user = Users.builder()
                .name(request.getName())
                .documentNumber(request.getDocumentNumber())
                .role(request.getRole())
                .email(request.getEmail())
                .password(request.getPassword())
                .enabled(true)
                .build();
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getDocumentNumber(), user.getRole(), user.isEnabled());
    }

    @Override
    public UserResponse update(UserRequest request, Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        user.setName(request.getName());
        user.setDocumentNumber(request.getDocumentNumber()); // Actualiza el documento si es necesario
        user.setRole(request.getRole());
        user.setEmail(request.getEmail()); // Actualiza el email si es necesario

        userRepository.save(user);

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getDocumentNumber(), user.getRole(), user.isEnabled());
    }

    @Override
    public UserResponse getById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getDocumentNumber(), user.getRole(), user.isEnabled());
    }

    @Override
    public UserResponse getByDocumentNumber(String documentNumber) {
        Users user = userRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getDocumentNumber(), user.getRole(), user.isEnabled());
    }

    @Override
    public List<UserResponse> getByRole(RoleEnum role) {
        List<Users> users = userRepository.findByRole(role);
        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getDocumentNumber(),
                        user.getRole(),
                        user.isEnabled()
                ))
                .collect(Collectors.toList());
    }


    // Método para eliminar un usuario por su ID
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
