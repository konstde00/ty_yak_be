package com.ty_yak.auth.mapper;

import com.sendgrid.Response;
import com.ty_yak.auth.model.dto.user.LanguageDto;
import com.ty_yak.auth.model.dto.user.SendGridResponseDto;
import com.ty_yak.auth.model.dto.user.UserGeneraInfoDto;
import com.ty_yak.auth.model.dto.user.UserSettingsDto;
import com.ty_yak.auth.model.entity.User;
import com.ty_yak.auth.model.enums.Language;
import com.ty_yak.auth.model.enums.UserRegistrationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import static com.ty_yak.auth.model.enums.Role.ROLE_USER;
import static java.time.LocalDateTime.now;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toEntity(String email, String name, String username, String password, UserRegistrationType registrationType) {
        return User
                .builder()
                .email(email)
                .name(name)
                .username(username)
                .password(password)
                .registrationType(registrationType)
                .roles(singletonList(ROLE_USER))
                .createdAt(now())
                .build();
    }


    public UserGeneraInfoDto toGeneralInfoDto(User user) {
        return UserGeneraInfoDto
                .builder()
                .id(user.getId())
                .languages(Arrays.stream(Language.values()).map(this::toLanguage).collect(toList()))
                .registrationType(user.getRegistrationType())
                .build();
    }

    public UserSettingsDto toSettingsDto(User user) {
        return UserSettingsDto
                .builder()
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .oldPassword("")
                .newPassword("")
                .country(user.getNativeCountryCode())
                .build();
    }

    public LanguageDto toLanguage(Language language) {
        return LanguageDto
                .builder()
                .name(language.getName())
                .nativeName(language.getNativeName())
                .emoji(language.getEmoji())
                .tag(language)
                .build();
    }

    public SendGridResponseDto toSendGridResponse(Response response, Long userId) {
        return SendGridResponseDto.builder()
                .userId(userId)
                .statusCode(response.getStatusCode())
                .body(response.getBody())
                .build();
    }
}
