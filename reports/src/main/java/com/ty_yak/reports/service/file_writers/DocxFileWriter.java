package com.ty_yak.reports.service.file_writers;

import com.ty_yak.reports.model.dto.exports.ExportRow;
import com.ty_yak.reports.model.enums.ReportFormat;
import com.ty_yak.reports.model.enums.ReportType;
import org.bouncycastle.util.io.BufferingOutputStream;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service("DOCX")
public class DocxFileWriter extends FileWriter {

    @Override
    public void write(List<String> headers, List<? extends ExportRow> data, ReportType reportType, ReportFormat reportFormat, HttpServletResponse httpServletResponse)
            throws IOException, Docx4JException {

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

        WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();

        ObjectFactory factory = Context.getWmlObjectFactory();

        Tbl table = factory.createTbl();

        Tr header = factory.createTr();
        for (int i = 0; i < headers.size(); i++) {
            addTableCell(header, headers.get(i), factory, wordPackage);
        }
        table.getContent().add(header);

        for (int i = 1; i < data.size(); i++) {
            Tr tableRow = factory.createTr();
            var content = data.get(i).getExportRow();
            for (int j = 0; j < headers.size(); j++) {
                addTableCell(tableRow, content.get(j).toString(), factory, wordPackage);
            }
            table.getContent().add(tableRow);
        }

        wordPackage.getMainDocumentPart().addObject(table);

        wordPackage.save(bufferingOutputStream);
        bufferingOutputStream.flush();
        bufferingOutputStream.close();
    }

    private static void addTableCell(Tr tableRow, String content,
                                     ObjectFactory factory, WordprocessingMLPackage wordPackage) {
        Tc tableCell = factory.createTc();
        tableCell.getContent().add(
                wordPackage.getMainDocumentPart().createParagraphOfText(content));
        tableRow.getContent().add(tableCell);
    }
}
