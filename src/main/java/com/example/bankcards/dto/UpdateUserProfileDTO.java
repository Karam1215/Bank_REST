package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "DTO для обновления профиля пользователя")
public record UpdateUserProfileDTO(

        @Size(min = 3, max = 255, message = "Имя должно быть от 3 до 255 символов.")
        @Schema(description = "Имя пользователя", example = "Иван")
        String firstName,

        @Size(min = 3, max = 255, message = "Отчество должно быть от 3 до 255 символов.")
        @Schema(description = "Отчество пользователя", example = "Иванович")
        String middleName,

        @Size(min = 3, max = 255, message = "Фамилия должна быть от 3 до 255 символов.")
        @Schema(description = "Фамилия пользователя", example = "Петров")
        String lastName,

        @Email(message = "Введите корректный email")
        @Schema(description = "Email пользователя", example = "ivan.petrov@example.com")
        String email,

        @Size(max = 18, message = "Номер телефона не должен превышать 18 символов.")
        @Pattern(
                regexp = "^\\+7\\s?\\(\\d{3}\\)\\s?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$",
                message = "Номер телефона должен быть в формате +7 (XXX) XXX-XX-XX"
        )
        @Schema(description = "Телефон пользователя", example = "+7 (999) 123-45-67")
        String phoneNumber,

        @Past(message = "Дата рождения должна быть в прошлом")
        @Schema(description = "Дата рождения пользователя", example = "1995-05-12")
        LocalDate birthDate,

        @Schema(description = "Пол пользователя", example = "MALE")
        String gender
) {}
