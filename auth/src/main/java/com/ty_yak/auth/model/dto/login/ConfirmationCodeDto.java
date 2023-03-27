package com.ty_yak.auth.model.dto.login;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@FieldDefaults(level = PRIVATE)
public class ConfirmationCodeDto {

    @NotBlank
    String email;

    int code;
}
