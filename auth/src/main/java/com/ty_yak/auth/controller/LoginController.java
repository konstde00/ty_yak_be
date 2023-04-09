package com.ty_yak.auth.controller;

import com.ty_yak.auth.model.dto.login.*;
import com.ty_yak.auth.model.dto.user.SendGridResponseDto;
import com.ty_yak.auth.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginController {

    LoginService loginService;

    @PostMapping("/v1/login/email")
    @Operation(summary = "User login by email")
    public JwtDto loginByEmail(@RequestBody @Valid LoginByEmailDto loginDto) {

        log.info("User tries to login with email - {}", loginDto.getEmail());

        var jwtDto = loginService.authorizationByEmailAndPassword(loginDto);

        log.info("User successfully login");

        return jwtDto;
    }

    @PostMapping("/v1/login/google")
    @Operation(summary = "User login by google")
    public JwtDto loginByGoogle(@RequestBody @Valid LoginByGoogleDto loginDto) {

        log.info("User try to login by google");

        var jwtDto = loginService.authorizationByGoogle(loginDto);

        log.info("User successfully login");

        return jwtDto;
    }

    @PostMapping("/v1/token/refresh")
    @Operation(summary = "Get token by refresh token")
    public JwtDto refreshToken(@RequestBody @Valid RefreshTokenDto refreshToken) {

        log.info("User try to refresh token");

        var jwtDto = loginService.refreshToken(refreshToken);

        log.info("User successfully refresh token");

        return jwtDto;
    }

    @PostMapping("/v1/logout")
    @Operation(summary = "User logout")
    public void logout(@AuthenticationPrincipal Long userId,
                       @RequestBody @Valid RefreshTokenDto refreshToken) {

        log.info("User with id - {} try to logout", userId);

        loginService.logout(userId, refreshToken);

        log.info("User successfully logout");
    }
}
