package com.ty_yak.auth.service.file_readers;

import com.ty_yak.auth.model.entity.User;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("docx")
public class DocxReader extends FileReader {

    @Override
    public List<User> readUsers(MultipartFile file) throws IOException {

        XWPFDocument doc = new XWPFDocument(file.getInputStream());
        List<XWPFTable> tables = doc.getTables();
        List<User> users = new ArrayList<>();

        for (XWPFTable table : tables) {
            for (int i = 1; i < table.getNumberOfRows(); i++) {
                XWPFTableRow row = table.getRow(i);

                String username = row.getCell(0).getText();

                String email = row.getCell(1).getText();

                String password = row.getCell(2).getText();

                String city = row.getCell(3).getText();

                users.add(new User(username, email, password, city));
            }
        }

        return users;
    }
}
