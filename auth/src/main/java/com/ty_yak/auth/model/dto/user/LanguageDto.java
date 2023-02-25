package com.ty_yak.auth.model.dto.user;

import com.ty_yak.auth.model.enums.Language;
import lombok.Builder;
import lombok.Value;

import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Value
@Builder
public class LanguageDto {

    String name;

    String nativeName;

    String emoji;

    @Enumerated(STRING)
    Language tag;
}
