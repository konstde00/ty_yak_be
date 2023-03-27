package com.ty_yak.reports.model.dto.exports;

import com.ty_yak.reports.model.enums.ReportFormat;
import com.ty_yak.reports.model.enums.ReportType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportRequestDto {

    ReportType reportType;
    ReportFormat reportFormat;
}
