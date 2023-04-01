package com.ty_yak.auth.controller;

import com.ty_yak.auth.model.dto.login.ConfirmChangePasswordCodeDto;
import com.ty_yak.auth.model.dto.login.ConfirmationCodeDto;
import com.ty_yak.auth.model.dto.registration.ChangePasswordDto;
import com.ty_yak.auth.model.dto.registration.RegistrationByGoogleDto;
import com.ty_yak.auth.model.dto.user.*;
import com.ty_yak.auth.model.dto.login.JwtDto;
import com.ty_yak.auth.model.dto.registration.RegistrationByEmailDto;
import com.ty_yak.auth.model.entity.User;
import com.ty_yak.auth.service.FileService;
import com.ty_yak.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    FileService fileService;
    UserService userService;

    @PostMapping("/v1/check/username")
    @Operation(summary = "Check if username already exists")
    public ResponseEntity<Boolean> checkUsername(@RequestBody @Valid CheckUsernameDto checkUsernameDto) {

        log.info("Check if username {} already exists", checkUsernameDto.getUsername());

        var exists = userService.checkUsername(checkUsernameDto);

        log.info("Check username already exists returned - {}", exists);

        return ResponseEntity.ok(exists);
    }

    @PostMapping("/v1/registration/email")
    @Operation(summary = "User registration by email")
    public ResponseEntity<JwtDto> registrationByEmail(@RequestBody @Valid RegistrationByEmailDto registrationDto) {

        log.info("User tries to register by email with params - {}", registrationDto);

        var jwtDto = userService.registrationByEmail(registrationDto);

        log.info("User successfully registered");

        return new ResponseEntity<>(jwtDto, HttpStatus.CREATED);
    }

    @PostMapping("/v1/registration/google")
    @Operation(summary = "User registration by google")
    public JwtDto registrationByGoogle(@RequestBody @Valid RegistrationByGoogleDto registrationDto) {

        log.info("User tries to register by google with params - {}", registrationDto);

        var jwtDto = userService.registrationByGoogle(registrationDto);

        log.info("User successfully registered");

        return jwtDto;
    }

    @PutMapping("/password/recovery")
    @Operation(summary = "Change password")
    public JwtDto recoverPassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {

        log.info("User with id - {} try to recover password", changePasswordDto.getUserId());

        var jwtDto = userService.recoverPassword(changePasswordDto);

        log.info("User recover password successfully.");

        return jwtDto;
    }

    @PostMapping("/v1/password-code/confirm")
    @Operation(summary = "Confirm change password code")
    public boolean confirmChangePasswordCode(@RequestBody @Valid ConfirmChangePasswordCodeDto confirmCodeDto) {

        log.info("User try to confirm code.");

        var confirmation = userService.confirmPasswordCode(confirmCodeDto);

        log.info("User confirmed code with - {}", confirmation);

        return confirmation;
    }

    @PostMapping("/v1/email-code/generate")
    @Operation(summary = "Send confirmation code to email")
    public SendGridResponseDto createConfirmationCode(@RequestBody @Valid ConfirmationCodeDto confirmationCodeDto) {

        log.info("User are trying to create confirmation code with params - {}", confirmationCodeDto);

        var sendGridResponse = userService.createConfirmationCode(confirmationCodeDto);

        log.info("SendGrid returned - {}", sendGridResponse);

        return sendGridResponse;
    }

    @PostMapping("/v1/email-code/confirm")
    @Operation(summary = "Confirm email confirmation code")
    public boolean confirmEmailCode(@RequestBody @Valid ConfirmationCodeDto confirmCodeDto) {

        log.info("User tries to confirm email code.");

        var confirmation
                = userService.confirmEmailCode(confirmCodeDto.getEmail(), confirmCodeDto.getCode());

        log.info("User confirmed code with - {}", confirmation);

        return confirmation;
    }

    @GetMapping("/v1/info")
    @Operation(summary = "Get user info")
    public UserDto getInfo(@AuthenticationPrincipal Long userId) {

        log.info("User with id - {} try to get own info", userId);

        var user = userService.getInfo(userId);

        log.info("Get info returned - {}", user);

        return user;
    }

    @PostMapping("/v1/avatar")
    @Operation(summary = "Upload avatar")
    public String uploadAvatar(@AuthenticationPrincipal Long userId,
                               MultipartFile avatar) {

        log.info("User with id - {} try to upload avatar - {}", userId, avatar);

        var url = userService.uploadAvatar(userId, avatar);

        log.info("Avatar uploaded with url - {}", url);

        return url;
    }

    @DeleteMapping("/v1/avatar")
    @Operation(summary = "Delete avatar")
    public ResponseEntity<?> deleteAvatar(@AuthenticationPrincipal Long userId) {

        log.info("User with id - {} try to delete own avatar", userId);

        userService.deleteAvatar(userId);

        log.info("Avatar deleted successfully");

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/v1/device-token")
    @Operation(summary = "Change device token")
    public ResponseEntity<String> changeUserDeviceToken(@AuthenticationPrincipal Long userId,
                                                        @RequestBody DeviceTokenDto deviceToken) {

        log.debug("Changing process of device token for user with id - {} is started", userId);

        userService.changeUserDeviceToken(deviceToken.getDeviceToken(), userId);

        log.debug("Successfully changed device token returned");

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/v1/device-token/reset")
    @Operation(summary = "Set device token to null")
    public ResponseEntity<?> resetDeviceToken(@AuthenticationPrincipal Long userId) {

        log.debug("Set device token to null for user with id - {} is started", userId);

        userService.resetUserDeviceToken(userId);

        log.debug("Successfully set device token to null");

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/v1/upload")
    @Operation(summary = "Upload file")
    public ResponseEntity<List<User>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException, Docx4JException {

        log.info("Upload has been requested");

        var result = fileService.readUsers(file);

        log.info("Upload has been completed successfully");

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/v1")
    @Operation(summary = "Delete user by id")
    public ResponseEntity<?> deleteUserById(@AuthenticationPrincipal Long userId) {

        log.debug("Deleting process of device token for user with id - {} is started", userId);

        userService.deleteUserById(userId);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
