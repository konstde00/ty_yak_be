package com.ty_yak.reports.service.file_writers;

import com.ty_yak.reports.model.dto.exports.ExportRow;
import com.ty_yak.reports.model.enums.ReportFormat;
import com.ty_yak.reports.model.enums.ReportType;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public abstract class FileWriter {

    protected static final String RESOURCES_DIRECTORY_PATH = "src/main/resources/";

    public abstract void write(List<String> headers, List<? extends ExportRow> data, ReportType reportType, ReportFormat reportFormat, HttpServletResponse httpServletResponse)
            throws IOException, Docx4JException;
}
