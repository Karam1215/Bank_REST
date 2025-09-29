package com.example.bankcards.cookies;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieUtil {
    public void addAuthTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("auth_token", token)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(604800)
            .sameSite("None")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
