package com.ty_yak.auth.model.dto.login;

import com.ty_yak.auth.model.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@FieldDefaults(level = PRIVATE)
public class JwtDto {

    String username;

    @ToString.Exclude
    String accessToken;

    @ToString.Exclude
    String refreshToken;

    String tokenType;

    Collection<Role> roles;

    LocalDateTime accessTokenExpirationTime;

    LocalDateTime accessTokenCreationTime;
}

