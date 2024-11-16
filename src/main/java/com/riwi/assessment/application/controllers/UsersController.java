package com.riwi.assessment.application.controllers;


import com.riwi.assessment.application.dtos.request.UserRequest;
import com.riwi.assessment.application.dtos.response.UserResponse;
import com.riwi.assessment.infrastructure.service.UserService;
import com.riwi.assessment.utils.enums.RoleEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management for the assessment system")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Create a new user",
            description = "Registers a new user in the system."
    )
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Validated @RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.create(userRequest);
        return ResponseEntity.ok(createdUser);
    }

    @Operation(
            summary = "Update an existing user",
            description = "Updates the details of an existing user identified by their ID."
    )
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @Validated @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = userService.update(userRequest, id);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes an existing user identified by their ID."
    )
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get a user by ID",
            description = "Fetches the details of a specific user by their ID."
    )
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        UserResponse userResponse = userService.getById(id);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Get a user by document number",
            description = "Fetches the details of a specific user by their document number."
    )
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @GetMapping("/document/{documentNumber}")
    public ResponseEntity<UserResponse> getUserByDocumentNumber(
            @Parameter(description = "Document number of the user to retrieve", required = true)
            @PathVariable String documentNumber) {
        UserResponse userResponse = userService.getByDocumentNumber(documentNumber);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Get users by role",
            description = "Fetches a list of users filtered by their role."
    )
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> getUsersByRole(
            @Parameter(description = "Role to filter users by", required = true)
            @PathVariable RoleEnum role) {
        List<UserResponse> users = userService.getByRole(role);
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "List all users",
            description = "Retrieves a paginated list of all users in the system."
    )
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @Parameter(description = "Page number for pagination", example = "0", required = true)
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10", required = true)
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<UserResponse> users = userService.getAll(page, size);
        return ResponseEntity.ok(users);
    }
}

