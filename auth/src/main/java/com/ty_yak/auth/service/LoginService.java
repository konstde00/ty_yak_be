package com.ty_yak.auth.service;

import com.ty_yak.auth.exception.NotValidException;
import com.ty_yak.auth.model.dto.login.*;
import com.ty_yak.auth.model.dto.user.SendGridResponseDto;
import com.ty_yak.auth.model.enums.UserRegistrationType;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import static com.ty_yak.auth.model.enums.BusinessLogicException.*;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LoginService {

    UserService userService;
    TokenService tokenService;
    GoogleService googleService;

    public JwtDto authorizationByEmailAndPassword(LoginByEmailDto loginDto) {

        log.info("'authorizationByEmailAndPassword' invoked with params - {}", loginDto);

        var user = userService.getByEmail(loginDto.getEmail());
        userService.validateUserRegistrationType(user, UserRegistrationType.EMAIL_AND_PASSWORD,
                WRONG_AUTHORIZATION_TYPE);

        validatePassword(loginDto.getPassword(), user.getPassword());

        var jwtDto = userService.generateTokens(user);

        log.info("'authorizationByEmailAndPassword' returned - {}", jwtDto);

        return jwtDto;
    }

    public JwtDto authorizationByGoogle(LoginByGoogleDto loginDto) {

        log.info("'authorizationByGoogle' invoked");

        var email = googleService.parseToken(loginDto.getToken());
        var user = userService.getByEmail(email);

        var jwtDto = userService.generateTokens(user);

        log.info("'authorizationByGoogle' returned - {}", jwtDto);

        return jwtDto;
    }

    public JwtDto refreshToken(RefreshTokenDto refreshTokenDto) {

        log.info("'refreshToken' invoked");

        var refreshToken = tokenService.refreshToken(refreshTokenDto.getRefreshToken());
        var jwtDto = tokenService.getAccessAndRefreshTokens(refreshToken.getUser(), refreshTokenDto.getRefreshToken());

        log.info("'refreshToken' returned 'Success'");

        return jwtDto;
    }

    public SendGridResponseDto generateConfirmationCode(ConfirmationCodeDto confirmationCodeDto) {

        log.info("'generateConfirmationCode' invoked with params - {}", confirmationCodeDto);
        var sendgridResponse = userService.createConfirmationCode(confirmationCodeDto);
        log.info("'generateConfirmationCode' returned - {}", sendgridResponse);

        return sendgridResponse;
    }

    public boolean confirmEmailCode(ConfirmationCodeDto confirmationCodeDto) {

        log.info("'confirmEmailCode' invoked with params - {}", confirmationCodeDto);
        var confirmation = userService.confirmEmailCode(confirmationCodeDto.getEmail(), confirmationCodeDto.getCode());
        log.info("'confirmEmailCode' returned - {}", confirmation);

        return confirmation;
    }

    public void logout(Long userId, RefreshTokenDto refreshTokenDto) {

        log.info("'logout' invoked for user with id - {}", userId);

        tokenService.revoke(userId, refreshTokenDto.getRefreshToken());

        log.info("'logout' returned 'Success'");
    }

    public static void validatePassword(String password){
        if(password.length() < 4 || password.length() > 25){
            log.warn("Password length is not valid - "+password.length());
            throw new NotValidException(NOT_VALID_PASSWORD.name());
        }
    }

    private void validatePassword(String inputPassword, String correctPassword) {

        var passwordValidation = BCrypt.checkpw(inputPassword, correctPassword);

        if (!passwordValidation) {
            log.error("Password is incorrect.");
            throw new NotValidException(INCORRECT_EMAIL_OR_PASSWORD.name());
        }
    }
}
