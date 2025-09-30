package com.example.bankcards.controller;

import com.example.bankcards.dto.CardBlockRequestDTO;
import com.example.bankcards.dto.UpdateUserProfileDTO;
import com.example.bankcards.dto.UserProfileDTO;
import com.example.bankcards.dto.UserRegistrationDTO;
import com.example.bankcards.service.CardBlockRequestService;
import com.example.bankcards.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "API для администратора", description = "Операции по управлению пользователями")
public class AdminController {

    private final ClientService userService;
    private final CardBlockRequestService cardBlockRequestService;
    @GetMapping("/users")
    @Operation(summary = "Получить всех пользователей с ролью USER", description = "Возвращает список всех пользователей, у которых роль USER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно возвращен"),
            @ApiResponse(responseCode = "204", description = "Пользователи с ролью USER не найдены")
    })
    public ResponseEntity<List<UserProfileDTO>> getAllUsersWithRoleUser() {
        return ResponseEntity.ok(userService.getAllUsersWithRoleUser().getBody());
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает информацию о пользователе по его UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно найден и возвращен"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не найден")
    })
    public ResponseEntity<UserProfileDTO> getUserById(@PathVariable("id") UUID userId) {
        UserProfileDTO userDTO = userService.getUserById(userId).getBody();
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Удалить пользователя по ID", description = "Администратор может удалить пользователя по его UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен — требуется роль ADMIN")
    })
    public ResponseEntity<String> deleteUserById(@PathVariable("id") String userId) {
        return userService.deleteClientByUsername(userId);
    }

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт новую учетную запись пользователя. Требуется уникальный email и имя пользователя."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь успешно зарегистрирован"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные входные данные",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email или имя пользователя уже существуют",
                    content = @Content
            )
    })
    public ResponseEntity<String> registerPlayer(@Valid @RequestBody UserRegistrationDTO dto) {
        log.info("Регистрация пользователя: {} {}", dto.firstName(), dto.lastName());
        return userService.createClient(dto);
    }

    @Operation(
        summary = "Обновление профиля пользователя",
        description = "Позволяет обновить профиль пользователя по его UUID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Профиль успешно обновлён"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    @PutMapping("/users/{userId}/profile")
    public ResponseEntity<String> updateUserProfile(
            @PathVariable String userId,
            @RequestBody UpdateUserProfileDTO updateUserProfileDTO) {

        log.info("Запрос на обновление профиля пользователя с ID: {}", userId);
        return userService.updateUserProfile(updateUserProfileDTO, userId);
    }

    @GetMapping("/clients/block-requests")
    public ResponseEntity<List<CardBlockRequestDTO>> getAllRequests() {
        return cardBlockRequestService.getAllRequests();
    }

}