package com.example.bankcards.service;

import com.example.bankcards.JWT.JWTService;
import com.example.bankcards.cookies.CookieUtil;
import com.example.bankcards.dto.UserLoginDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.Role;
import com.example.bankcards.exception.InvalidCredentialsException;
import com.example.bankcards.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final JWTService jwtService;
    private final CookieUtil cookieUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

/*
    public ResponseEntity<String> createAdmin(UserRegistrationDTO registrationDTO) {
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
*/

    public ResponseEntity<String> loginForAdmin(UserLoginDTO userLoginDTO, HttpServletResponse response) {
        log.info("Попытка входа по email: {}", userLoginDTO.email());

        User user = userRepository.findUserByEmail(userLoginDTO.email())
                .orElseThrow(() -> {
                    log.warn("Неверная попытка входа: {}", userLoginDTO.email());
                    return new InvalidCredentialsException("Неверный email или пароль");
                });
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new InvalidCredentialsException("Неверный email или пароль");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserId(), userLoginDTO.password())
        );

        String token = jwtService.generateAccessToken(user.getUserId(), String.valueOf(Role.ADMIN));
        log.info("Вход выполнен успешно: {} {}, Id: {}", user.getFirstName(), user.getLastName(), user.getUserId());

        cookieUtil.addAuthTokenCookie(response, token);
        return ResponseEntity.ok("🎊 Вход выполнен успешно! Токен сохранён в cookie.");
    }

}
