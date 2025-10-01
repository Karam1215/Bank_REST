package com.example.bankcards.entity;

import com.example.bankcards.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Schema(description = "Сущность пользователя")
public class User {

    @Id
    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(name = "user_id", example = "72bde859-d59e-4a61-b060-d5fa60426203", requiredMode = Schema.RequiredMode.REQUIRED, description = "Уникальный идентификатор пользователя.")
    private UUID userId;

    @NotBlank(message = "Имя не может быть пустым")
    @Column(name = "first_name", nullable = false)
    @Size(min = 3, max = 255, message = "Имя должно быть от 3 до 255 символов.")
    @Schema(name = "firstName", example = "Карам", requiredMode = Schema.RequiredMode.REQUIRED, description = "Имя пользователя.")
    private String firstName;

    @Column(name = "middle_name")
    @Size(min = 3, max = 255, message = "Отчество должно быть от 3 до 255 символов.")
    @Schema(name = "middleName", example = "Ахмедович", description = "Отчество пользователя.")
    private String middleName;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Column(name = "last_name", nullable = false)
    @Size(min = 3, max = 255, message = "Фамилия должна быть от 3 до 255 символов.")
    @Schema(name = "lastName", example = "Карам", requiredMode = Schema.RequiredMode.REQUIRED, description = "Фамилия пользователя.")
    private String lastName;

    @NotBlank(message = "Password can't be empty")
    @Column(name = "password")
    @Size(min = 7, max = 255, message = "Password should be between 7 and 255 characters.")
    @Schema(name = "password", example = "pass123123", requiredMode = Schema.RequiredMode.REQUIRED, description = "User's password. Should be between 7 and 255 characters.")
    private String password;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Введите корректный email")
    @Column(name = "email", unique = true, nullable = false)
    @Schema(name = "email", example = "2002@hotmail.com", requiredMode = Schema.RequiredMode.REQUIRED, description = "Email пользователя. Должен быть уникальным.")
    private String email;

    @Column(name = "phone_number", unique = true, nullable = false)
    @Size(max = 18, message = "Номер телефона не должен превышать 18 символов.")
    @Pattern(regexp = "^\\+7\\s?\\(\\d{3}\\)\\s?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$", message = "Номер телефона должен быть в формате +7 (XXX) XXX-XX-XX")
    @Schema(name = "phoneNumber", example = "+7 (123) 333-22-11", description = "Телефон пользователя. Формат: +7 (XXX) XXX-XX-XX.")
    private String phoneNumber;

    @Past(message = "Дата рождения должна быть в прошлом")
    @Column(name = "date_of_birth")
    @Schema(name = "birthDate", example = "2002-11-04", description = "Дата рождения пользователя. Формат: yyyy-MM-dd.")
    private LocalDate birthDate;

    @NotNull(message = "Роль пользователя не может быть пустой")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Schema(name = "role", example = "USER", description = "Роль пользователя.")
    private Role role;

    //TODO: можно заменить на Enum Gender
    @Column(name = "gender")
    @Schema(name = "gender", example = "MALE", description = "Пол пользователя. Возможные значения: 'MALE', 'FEMALE'.")
    private String gender;

    @Column(name = "created_at", updatable = false)
    @Schema(name = "createdAt", example = "2025-02-07T14:30:00", description = "Дата и время создания пользователя.")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(name = "updatedAt", example = "2025-02-07T14:30:00", description = "Дата и время последнего обновления пользователя.")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}