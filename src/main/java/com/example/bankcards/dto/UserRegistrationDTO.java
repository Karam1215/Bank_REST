package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRegistrationDTO(

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 3, max = 255, message = "Имя должно быть от 3 до 255 символов.")
    @Schema(name = "firstName", example = "Карам", requiredMode = Schema.RequiredMode.REQUIRED, description = "Имя пользователя.")
    String firstName,

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 3, max = 255, message = "Фамилия должна быть от 3 до 255 символов.")
    @Schema(name = "lastName", example = "Карам", requiredMode = Schema.RequiredMode.REQUIRED, description = "Фамилия пользователя.")
    String lastName,

    @NotBlank(message = "Password can't be empty")
    @Size(min = 7, max = 255, message = "Password should be between 7 and 255 characters.")
    @Schema(name = "password", example = "pass123123", requiredMode = Schema.RequiredMode.REQUIRED, description = "User's password. Should be between 7 and 255 characters.")
    String password,

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Введите корректный email")
    @Schema(name = "email", example = "2002@hotmail.com", requiredMode = Schema.RequiredMode.REQUIRED, description = "Email пользователя. Должен быть уникальным.")
    String email,    @Size(min = 7, max = 255, message = "Password should be between 7 and 255 characters.")


    @Size(max = 18, message = "Номер телефона не должен превышать 18 символов.")
    @Pattern(regexp = "^\\+7\\s?\\(\\d{3}\\)\\s?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$", message = "Номер телефона должен быть в формате +7 (XXX) XXX-XX-XX")
    @Schema(name = "phoneNumber", example = "+7 (123) 333-22-11", description = "Телефон пользователя. Формат: +7 (XXX) XXX-XX-XX.")
    String phoneNumber,

    @Past(message = "Дата рождения должна быть в прошлом")
    @Schema(name = "birthDate", example = "2002-11-04", description = "Дата рождения пользователя. Формат: yyyy-MM-dd.")
    LocalDate birthDate,

    @Schema(name = "gender", example = "MALE", description = "Пол пользователя. Возможные значения: 'MALE', 'FEMALE'.")
    String gender
) {
}
