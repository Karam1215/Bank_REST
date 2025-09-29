package com.example.bankcards.controller;

import com.example.bankcards.JWT.JWTService;
import com.example.bankcards.dto.ChangePasswordRequest;
import com.example.bankcards.dto.UserLoginDTO;
import com.example.bankcards.dto.UserRegistrationDTO;
import com.example.bankcards.service.AdminService;
import com.example.bankcards.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/admin")
@Tag(name = "getting Account API", description = "operations connected with user register/login")
public class AdminAuthController {

    private final AdminService adminService;
    private final JWTService jwtService;

/*    @PostMapping("/register")
    @Operation(summary = "Register a new player", description = "Creates a new player account. Requires unique email and username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "409", description = "Email or username already exists", content = @Content)
    })
    public ResponseEntity<String> registerPlayer(@Valid @RequestBody UserRegistrationDTO dto) {
        log.info("Registering player: {}", dto.firstName() + " " + dto.lastName());
        return clientService.createClient(dto);
    }*/

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates the player and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(value = "{\"token\": \"jwt-token-string\"}")
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials", content = @Content)
    })
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        log.info("Login attempt for: {}", userLoginDTO.email());
        return adminService.loginForAdmin(userLoginDTO, response);
    }

    @GetMapping("/validate")
    public void validateToken(@RequestParam("token") String token) {
        jwtService.validateToken(token);
    }

}