package com.ty_yak.auth.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.ty_yak.auth.exception.AlreadyExistException;
import com.ty_yak.auth.exception.ExpiredException;
import com.ty_yak.auth.exception.NotValidException;
import com.ty_yak.auth.exception.ResourceNotFoundException;
import com.ty_yak.auth.mapper.UserMapper;
import com.ty_yak.auth.model.dto.login.ConfirmChangePasswordCodeDto;
import com.ty_yak.auth.model.dto.registration.ChangePasswordDto;
import com.ty_yak.auth.model.dto.registration.RegistrationByGoogleDto;
import com.ty_yak.auth.model.dto.user.CheckUsernameDto;
import com.ty_yak.auth.model.dto.login.JwtDto;
import com.ty_yak.auth.model.dto.login.ConfirmationCodeDto;
import com.ty_yak.auth.model.dto.registration.RegistrationByEmailDto;
import com.ty_yak.auth.model.dto.user.SendGridResponseDto;
import com.ty_yak.auth.model.dto.user.UserDto;
import com.ty_yak.auth.model.entity.ConfirmationCode;
import com.ty_yak.auth.model.entity.User;
import com.ty_yak.auth.model.enums.BusinessLogicException;
import com.ty_yak.auth.model.enums.UserRegistrationType;
import com.ty_yak.auth.repository.ConfirmationCodeRepository;
import com.ty_yak.auth.repository.DeviceTokenRepository;
import com.ty_yak.auth.repository.UserRepository;
import com.ty_yak.auth.util.DateUtil;
import com.ty_yak.auth.util.SecureRandomUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.sendgrid.Method.POST;
import static com.ty_yak.auth.model.enums.BusinessLogicException.*;
import static com.ty_yak.auth.model.enums.UserRegistrationType.EMAIL_AND_PASSWORD;
import static com.ty_yak.auth.model.enums.UserRegistrationType.GOOGLE;
import static java.lang.String.format;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    @NonFinal
    @Value("${sendgrid.sender}")
    String sendGridSender;

    SendGrid sendGrid;
    UserMapper userMapper;
    FileService fileService;
    TokenService tokenService;
    EmailService emailService;
    GoogleService googleService;
    UserRepository userRepository;
    DeviceTokenRepository deviceTokenRepository;
    ConfirmationCodeRepository confirmationCodeRepository;

    public void validateEmails(List<String> emails) {
        emails.forEach(this::validateEmail);
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistException("User with email - " + email + " already exists");
        }
    }

    public User getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new NotValidException("Not found a user with id " + id));
    }

    public User getByEmail(String email) {

        log.info("'getByEmail' invoked with email - {}", email);

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error(format("User with email - %s does not exist. ", email));
                    return new ResourceNotFoundException(INCORRECT_EMAIL_OR_PASSWORD.name());
                });

        log.info("'getByEmail' returned 'Success'");

        return user;
    }

    @Transactional(readOnly = true)
    public Boolean checkUsername(CheckUsernameDto checkUsernameDto) {

        log.info("'checkUsername' invoked with params - {}", checkUsernameDto);

        var exists = userRepository.existsByUsername(checkUsernameDto.getUsername());

        log.info("'checkUsername' returned - {}", exists);

        return exists;
    }

    public void validateUserRegistrationType(User user, UserRegistrationType requiredType, BusinessLogicException exceptionType) {
        if (!user.getRegistrationType().equals(requiredType)) {
            log.warn("Wrong type of authorization - " + user.getRegistrationType());
            throw new NotValidException(exceptionType.name());
        }
    }

    @SneakyThrows
    @Transactional
    public JwtDto registrationByEmail(RegistrationByEmailDto registrationDto) {

        log.info("'registrationByEmail' invoked with params - {}", registrationDto);

        var user = createNew(registrationDto.getEmail(), registrationDto.getPassword(),
                registrationDto.getName(), registrationDto.getUsername(), EMAIL_AND_PASSWORD);

        var jwtDto = generateTokens(user);

        log.info("'registrationByEmail' returned - {}", jwtDto);

        return jwtDto;
    }

    public JwtDto registrationByGoogle(RegistrationByGoogleDto registrationDto) {

        log.info("'registrationByGoogle' invoked with params - {}", registrationDto);

        var email = googleService.parseToken(registrationDto.getToken());

        var user = createNew(email, "", email, email, GOOGLE);

        var jwtDto = generateTokens(user);

        log.info("'registrationByGoogle' returned - {}", jwtDto);

        return jwtDto;
    }

    @Transactional(readOnly = true)
    public UserDto getInfo(Long userId) {

        log.info("'getInfo' invoked with user id - {}", userId);

        var user = getById(userId);

        var userGeneralInfo = userMapper.toGeneralInfoDto(user);
        var userSettingsDto = userMapper.toSettingsDto(user);

        return UserDto
                .builder()
                .general(userGeneralInfo)
                .settings(userSettingsDto)
                .build();
    }

    @Transactional
    public void deleteAvatar(Long userId) {

        log.info("'deleteAvatar' invoked with user id - {}", userId);

        var user = getById(userId);
        fileService.deleteByUrl(user.getAvatarUrl());

        user.setAvatarUrl(null);

        log.info("'deleteAvatar' returned 'Success'");
    }

    @Transactional
    public void changeUserDeviceToken(String deviceToken, Long userId) {

        deviceTokenRepository.updateDeviceToken(deviceToken, userId);
    }

    @Transactional
    public void resetUserDeviceToken(Long userId) {

        deviceTokenRepository.updateDeviceToken(null, userId);
    }

    private String generatePassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        return gen.generatePassword(6, lowerCaseRule,
                upperCaseRule, digitRule);
    }

    public User createNew(String email, String password, String name, String username, UserRegistrationType type) {

        log.info("'createNew' invoked with email - {} and type - {}",
                email, type);

        checkEmailDuplication(email);

        var user = switch (type) {
            case EMAIL_AND_PASSWORD -> userRepository.save(userMapper
                    .toEntity(email, name, username, bcryptPassword(password), type));
            case GOOGLE, FACEBOOK, APPLE -> userRepository.save(userMapper
                    .toEntity(email, name, username, "", type));
        };

        log.info("'createNew' returned - {}", user);

        return user;
    }

    @SneakyThrows
    @Transactional
    public void recoverPassword(String email) {

        checkEmail(email);

        var user = getByEmail(email);

        ConfirmationCode code = generateConfirmationCode(user);
        confirmationCodeRepository.save(code);

        emailService.sendRecoveryCode(email, code.getCode());
    }

    private void checkEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotValidException(INCORRECT_EMAIL_OR_PASSWORD.name());
        }
    }

    private ConfirmationCode generateConfirmationCode(User user) {
        return ConfirmationCode.builder()
                .user(user)
                .code(generateCode())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordDto changePasswordDto) {

        log.info("'changePassword' invoked with params - {} and user id - {}", changePasswordDto, userId);

        var user = getById(userId);

        validateUserRegistrationType(user, EMAIL_AND_PASSWORD, WRONG_AUTHORIZATION_TYPE_FOR_PASSWORD_RECOVERING);

        user.setPassword(bcryptPassword(changePasswordDto.getNewPassword()));

        log.info("'changePassword' returned 'Success'");
    }

    @Transactional
    public void deleteUserById(Long userId) {
        // log out by socket

        var user = userRepository.findById(userId)
                .orElseThrow(() -> {

                    var message = format("User with id - %d does not exist.", userId);
                    log.error(message);

                    return new ResourceNotFoundException(message);
                });

        // actually delete user
        userRepository.deleteUserById(user.getId());
    }

    public Mail generateInfo(String email, String password) {

        log.info("'generateInfo' invoked with email - {} and subject - Confirm email", email);

        var from = new Email(sendGridSender);
        var to = new Email(email);

        var content = new Content("text/html", "Please, provide this password to confirm your email: " + password);

        log.info("'generateInfo' returned 'Success'");

        return new Mail(from, "Confirm email", to, content);
    }

    private Mail generateInfo(String email, String subject, String value, int code) {

        log.info("'generateInfo' invoked with email - {} and subject - {}", email, subject);

        var from = new Email(sendGridSender);
        var to = new Email(email);

        var content = new Content("text/html", value + code);

        log.info("'generateInfo' returned 'Success'");

        return new Mail(from, subject, to, content);
    }

    @SneakyThrows(IOException.class)
    private Request generateRequest(Mail mail) {

        var request = new Request();
        request.setMethod(POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        return request;
    }

    public JwtDto generateTokens(User user) {

        var refreshToken = tokenService.generateRefreshToken(user);

        return tokenService.getAccessAndRefreshTokens(user, refreshToken);
    }

    @SneakyThrows
    @Transactional
    public SendGridResponseDto createConfirmationCode(ConfirmationCodeDto confirmationCodeDto) {

        log.info("'createChangePasswordCode' invoked with params - {}", confirmationCodeDto);

        var email = confirmationCodeDto.getEmail();
        var confirmationCode = confirmationCodeRepository
                .findByEmailAndCode(email, confirmationCodeDto.getCode());

        var code = generateCode();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    var message = "User with such email is not found.";
                    log.warn(message);
                    return new ResourceNotFoundException(message);
                });

        if (confirmationCode.isPresent()) {

            confirmationCode.get().setCode(code);
            confirmationCode.get().setExpiredAt(DateUtil.getLocalDateTimeNow().plusMinutes(30L));

        } else {
            confirmationCodeRepository.save(ConfirmationCode
                    .builder()
                    .code(code)
                    .user(user)
                    .expiredAt(DateUtil.getLocalDateTimeNow().plusHours(2))
                    .build());
        }

        var mail = generateInfo(email, "Confirm email", "Please, provide this code to confirm your email: ", code);
        var request = generateRequest(mail);
        var response = sendGrid.api(request);

        var sendGridResponseDto = userMapper.toSendGridResponse(response, null);

        log.info("'createChangePasswordCode' returned - {}", sendGridResponseDto);

        return sendGridResponseDto;
    }

    @Transactional(readOnly = true)
    public boolean confirmEmailCode(String email, int code) {

        log.info("'confirmEmailCode' invoked with email - {}", email);

        var userCode = confirmationCodeRepository.findByEmailAndCode(email, code)
                .orElseThrow(() -> {
                    log.error("Wrong code.");
                    return new ResourceNotFoundException(INCORRECT_CONFIRM_CODE.name());
                });

        if (userCode.getExpiredAt().isBefore(DateUtil.getLocalDateTimeNow())) {
            log.error("Your code has expired.");
            throw new ExpiredException(CODE_EXPIRED.name());
        }

        log.info("'confirmEmailCode' returned - {}", true);

        return true;
    }

    @Transactional
    public String uploadAvatar(Long userId, MultipartFile avatar) {

        log.info("'uploadAvatar' invoked with user id - {} and avatar - {}", userId, avatar);

        var user = getById(userId);
        var url = fileService.saveUserAvatar(userId, avatar);

        if (nonNull(user.getAvatarUrl()))
            fileService.deleteByUrl(user.getAvatarUrl());

        user.setAvatarUrl(url);

        log.info("'uploadAvatar' returned - {}", url);

        return url;
    }

    public List<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    private int generateCode() {
        return 1000 + SecureRandomUtil.getRandom(9000);
    }

    private void checkEmailDuplication(String email) {

        if (userRepository.existsByEmail(email)) {
            log.error(format("User with email %s already exist. ", email));
            throw new AlreadyExistException(EMAIL_ALREADY_EXISTS.name());
        }
    }

    private String bcryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
