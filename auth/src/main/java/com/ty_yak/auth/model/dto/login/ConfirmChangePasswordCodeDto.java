package com.ty_yak.auth.model.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmChangePasswordCodeDto {

    @NotNull
    private Long userId;

    @NotNull
    private Integer code;
}
