package com.ty_yak.auth.model.dto.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {

    UserGeneraInfoDto general;

    UserSettingsDto settings;
}
