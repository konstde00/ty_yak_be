package com.ty_yak.auth.model.dto.user;

import com.ty_yak.auth.model.enums.CountryCode;
import lombok.Builder;
import lombok.Value;

import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Value
@Builder
public class UserProfileDto {

    Long userId;

    String username;

    String avatarUrl;

    boolean online;

    @Enumerated(STRING)
    CountryCode country;

    String lastOnline;
}
