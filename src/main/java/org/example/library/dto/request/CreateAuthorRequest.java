package org.example.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateAuthorRequest(
        @NotBlank(message = "Имя автора не должно быть пустым")
        @Size(max = 100, message = "Имя не должно превышать 100 символов")
        String firstName,

        @NotBlank(message = "Фамилия автора не должна быть пустой")
        @Size(max = 100, message = "Фамилия не должна превышать 100 символов")
        String lastName,

        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate birthDate,

        @Size(max = 100, message = "Название страны не должно превышать 100 символов")
        String country
) {}