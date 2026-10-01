package org.example.library.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateAuthorRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        LocalDate birthDate,
        String country
) {}