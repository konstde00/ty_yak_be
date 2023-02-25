package com.ty_yak.auth.model.dto.user;

import com.ty_yak.auth.model.enums.CountryCode;
import lombok.Builder;
import lombok.Value;

import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Value
@Builder
public class UserSettingsDto {

    String username;

    String oldPassword;

    String newPassword;

    String avatarUrl;

    @Enumerated(STRING)
    CountryCode country;

    LanguageDto language;
}
