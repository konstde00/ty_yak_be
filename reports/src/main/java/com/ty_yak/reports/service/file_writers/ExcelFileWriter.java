package com.ty_yak.reports.service.file_writers;

import com.ty_yak.reports.model.dto.exports.ExportRow;
import com.ty_yak.reports.model.enums.ReportFormat;
import com.ty_yak.reports.model.enums.ReportType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.util.io.BufferingOutputStream;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service("XLSX")
public class ExcelFileWriter extends FileWriter {

    @Override
    public void write(List<String> headers, List<? extends ExportRow> data, ReportType reportType, ReportFormat reportFormat, HttpServletResponse httpServletResponse) throws IOException {
        String fileName = reportType.getFileName();

        Path filePath = Paths.get(RESOURCES_DIRECTORY_PATH + fileName + reportFormat.getExtension());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        BufferingOutputStream bufferingOutputStream
                = new BufferingOutputStream(httpServletResponse.getOutputStream());

        String contentType = reportFormat.getContentType();
        httpServletResponse.setContentType(contentType);
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename="
                + fileName + reportFormat.getExtension());
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(reportType.getFileName());

        sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            sheet.getRow(0).createCell(i).setCellValue(headers.get(i));
        }

        for (int i = 0; i < data.size(); i++) {
            sheet.createRow(i + 1);
            var row = data.get(i).getExportRow();
            for (int j = 0; j < headers.size(); j++) {
                sheet.getRow(i + 1).createCell(j).setCellValue(row.get(j).toString());
            }
        }

        workbook.write(bufferingOutputStream);
        bufferingOutputStream.flush();
        bufferingOutputStream.close();
    }
}
