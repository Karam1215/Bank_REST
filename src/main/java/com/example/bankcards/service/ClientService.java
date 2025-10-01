package com.example.bankcards.service;

import com.example.bankcards.JWT.JWTService;
import com.example.bankcards.cookies.CookieUtil;
import com.example.bankcards.dto.*;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.Role;
import com.example.bankcards.exception.EmailAlreadyExistException;
import com.example.bankcards.exception.InvalidCredentialsException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.mappers.UserMapper;
import com.example.bankcards.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    private final JWTService jwtService;
    private final CookieUtil cookieUtil;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<String> createClient(UserRegistrationDTO registrationDTO) {
        log.info("Попытка регистрации пользователя: {} {}", registrationDTO.firstName(), registrationDTO.lastName());

        if (userRepository.findUserByEmail(registrationDTO.email()).isPresent()) {
            log.warn("Электронная почта уже существует: {}", registrationDTO.email());
            throw new EmailAlreadyExistException("Электронная почта уже существует");
        }

        User userToBeSaved = User.builder()
                .firstName(registrationDTO.firstName())
                .lastName(registrationDTO.lastName())
                .email(registrationDTO.email())
                .password(passwordEncoder.encode(registrationDTO.password()))
                .phoneNumber(registrationDTO.phoneNumber())
                .birthDate(registrationDTO.birthDate())
                .gender(registrationDTO.gender())
                .role(Role.USER)
                .build();

        userRepository.save(userToBeSaved);

        log.info("Пользователь успешно зарегистрирован: {} {}", userToBeSaved.getFirstName(), userToBeSaved.getLastName());

        return new ResponseEntity<>("🎊 Добро пожаловать! Ваш аккаунт создан." +
                " Пожалуйста, проверьте свою электронную почту для подтверждения и начните " +
                        "пользоваться сервисом! 🚀", HttpStatus.CREATED);
    }

    public ResponseEntity<String> login(UserLoginDTO userLoginDTO, HttpServletResponse response) {
        log.info("Попытка входа по email: {}", userLoginDTO.email());

        User user = userRepository.findUserByEmail(userLoginDTO.email())
                .orElseThrow(() -> {
                    log.warn("Неверная попытка входа: {}", userLoginDTO.email());
                    return new InvalidCredentialsException("Неверный email или пароль");
                });
        if(user.getRole().equals(Role.ADMIN)) {
            throw new InvalidCredentialsException("Неверный email или пароль");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserId(), userLoginDTO.password())
        );

        String token = jwtService.generateAccessToken(user.getUserId(), String.valueOf(Role.USER));
        log.info("Вход выполнен успешно: {} {}, Id: {}", user.getFirstName(), user.getLastName(), user.getUserId());

        cookieUtil.addAuthTokenCookie(response, token);
        return ResponseEntity.ok("🎊 Вход выполнен успешно! Токен сохранён в cookie.");
    }

    public ResponseEntity<String> changePassword(Authentication authentication,
                                                     ChangePasswordRequest changePasswordRequest) {

        User user = userRepository.findUserByUserId(UUID.fromString(authentication.getName())).get();

        if (!passwordEncoder.matches(changePasswordRequest.currentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        if (!changePasswordRequest.newPassword().equals(changePasswordRequest.passwordConfirmation())) {
            throw new ValidationException("Confirmed password does not match.");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
        userRepository.save(user);
        log.info("Password changed successfully");
        return ResponseEntity.ok("Password changed successfully.");
    }


    public ResponseEntity<List<UserProfileDTO>> getAllUsersWithRoleUser() {
        List<User> users = userRepository.findAllByRole(Role.USER);

        return ResponseEntity.ok(userMapper.toDtoList(users));
    }

    public ResponseEntity<UserProfileDTO> getUserById(UUID userId) {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("can not find user: {}", userId );
                    return new UserNotFoundException("can not find user");
                });

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    public ResponseEntity<String> deleteClientByUsername(String userId) {
        log.info("Attempting to delete account for: {}", userId);
        UUID uuid = UUID.fromString(userId);
        User userToBeDeleted = userRepository.findUserByUserId(uuid).orElseThrow(() -> {
            log.warn("Player not found for deletion: {}", userId);
            throw new UserNotFoundException("Player not found for deletion");
        });

        userRepository.delete(userToBeDeleted);
        log.info("Player account deleted: {} 🎉🎉🎉🎉🎉🎉", userId);

        return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<String> updateUserProfile(UpdateUserProfileDTO updateUserProfileDTO,
                                                        String userId) {
        log.info("Updating profile for player: {}", userId);
        UUID uuid = UUID.fromString(userId);
        User user = userRepository.findUserByUserId(uuid).orElseThrow(() -> {
            log.warn("User not found for profile update: {}", userId);
             throw new UserNotFoundException("User not found for profile update");
        });

        userMapper.updateUserFromDto(updateUserProfileDTO, user);
        userRepository.save(user);

        log.info("Profile updated successfully for player: {} 🎉🎉🎉🎉🎉🎉", user.getFirstName());
        return new ResponseEntity<>("Profile updated successfully", HttpStatus.OK);
    }
}
