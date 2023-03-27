package com.ty_yak.reports.controller;

import com.ty_yak.reports.model.dto.reports.ReportResponseDto;
import com.ty_yak.reports.model.enums.ReportFormat;
import com.ty_yak.reports.model.enums.ReportType;
import com.ty_yak.reports.service.ExportService;
import com.ty_yak.reports.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/reports")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

    ReportService reportService;
    ExportService exportService;

    @GetMapping("/v1")
    @Operation(summary = "Get report data")
    public ResponseEntity<ReportResponseDto> getReportData(@RequestParam ReportType reportType) {

        log.info("Report {} has been requested", reportType);

        var report = reportService.getReportData(reportType);

        log.info("Report {} has been prepared successfully", reportType);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/v1/export")
    @Operation(summary = "Execute file export")
    public void executeExport(@RequestParam ReportType reportType,
                              @RequestParam ReportFormat reportFormat,
                              HttpServletResponse httpServletResponse) throws IOException, Docx4JException {

        log.info("Export {} with format {} has been requested", reportType, reportFormat);

        exportService.executeExport(reportType, reportFormat, httpServletResponse);

        log.info("Export {} with format {} has been completed successfully", reportType, reportFormat);
    }
}
