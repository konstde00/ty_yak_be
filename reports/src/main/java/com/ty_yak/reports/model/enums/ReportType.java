package com.ty_yak.reports.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum ReportType {

    MOST_ACTIVE_USERS(
            "MostActiveUsers",
            List.of("Username", "Email", "City", "Messages sent")
    );

    private final String fileName;
    private final List<String> headers;
}
