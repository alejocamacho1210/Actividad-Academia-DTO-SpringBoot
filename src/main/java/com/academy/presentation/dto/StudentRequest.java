package com.academy.presentation.dto;

import com.academy.persistence.entity.StudentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudentRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotNull(message = "Age is required")
        @Min(value = 16, message = "Age must be at least 16")
        @Max(value = 99, message = "Age must be at most 99")
        Integer age,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotNull(message = "Status is required")
        StudentStatus status
) {
}
