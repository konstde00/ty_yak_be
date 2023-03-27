package com.ty_yak.auth.model.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class LoginByEmailDto {

    @NotBlank(message = "Email may not be blank")
    String email;

    @ToString.Exclude
    @NotBlank(message = "Password may not be blank")
    String password;
}
