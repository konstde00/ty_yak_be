package com.ty_yak.reports.service;

import com.ty_yak.reports.model.dto.exports.ExportRow;
import com.ty_yak.reports.model.enums.ReportFormat;
import com.ty_yak.reports.model.enums.ReportType;
import com.ty_yak.reports.service.file_writers.FileWriter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExportService {

    ReportService reportService;
    Map<String, FileWriter> fileWriters;

    public void executeExport(ReportType reportType, ReportFormat reportFormat,
                              HttpServletResponse httpServletResponse) throws IOException, Docx4JException {

        List<String> headers = reportType.getHeaders();
        List<? extends ExportRow> exportData = reportService.getExportData(reportType);

        FileWriter fileWriter = fileWriters.get(reportFormat.name());
        fileWriter.write(headers, exportData, reportType, reportFormat, httpServletResponse);
    }
}
