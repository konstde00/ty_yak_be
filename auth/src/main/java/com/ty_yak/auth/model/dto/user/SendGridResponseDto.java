package com.ty_yak.auth.model.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Value
@Builder
public class SendGridResponseDto {

    Long userId;

    Integer statusCode;

    String body;
}
