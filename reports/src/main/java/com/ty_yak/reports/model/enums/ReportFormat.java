package com.ty_yak.reports.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ReportFormat {

    XLSX(".xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    DOCX(".docx",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private final String extension;
    private final String contentType;
}
