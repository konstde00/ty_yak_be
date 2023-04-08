package com.ty_yak.social.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    STATUS_UPDATE("Status Update");

    private final String subject;
}
