package com.ty_yak.auth.service.file_readers;

import com.ty_yak.auth.exception.NotValidException;
import com.ty_yak.auth.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service("xlsx")
public class ExcelReader extends FileReader {

    @Override
    public List<User> readUsers(MultipartFile file) {

        Workbook workbook = getWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        List<User> users = new ArrayList<>();

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);

//            checkUserRowFormat(row);

            String username = getStringCellValueIfPossible(row, 0,
                    "Username");

            String email = getStringCellValueIfPossible(row, 1,
                    "Email");

            String password = getStringCellValueIfPossible(row, 2,
                    "Password");

            String city = getStringCellValueIfPossible(row, 3,
                    "City");

            users.add(new User(username, email, password, city));

        }

        return users;
    }

    private Workbook getWorkbook(MultipartFile file) {
        try {
            return new XSSFWorkbook(file.getInputStream());
        } catch (Exception e) {
            throw new NotValidException(String.format("Can't read from file %s",
                    file.getOriginalFilename()));
        }
    }

    private String getStringCellValueIfPossible(Row row, Integer cellIndex, String variableName) {
        String variableValue;
        try {
            variableValue = row.getCell(cellIndex).getStringCellValue();
        } catch (Exception e) {
            throw new NotValidException(generateErrorMessageForIncorrectFormat(variableName, (row.getRowNum()) + 1));
        }
        if (variableValue == null) {
            throw new NotValidException(generateErrorMessageIfMissed(variableName, (row.getRowNum()) + 1));
        }
        variableValue = variableValue.trim();
        return variableValue;
    }

    private String generateErrorMessageForIncorrectFormat(String variableName, Integer rowNumber) {
        return "Incorrect " +
                variableName.substring(0, 1).toLowerCase(Locale.ROOT) +
                variableName.substring(1) +
                " format" +
                " in row " +
                rowNumber;
    }

    private String generateErrorMessageIfMissed(String variableName, Integer rowNumber) {
        return "Missed " +
                variableName.substring(0, 1).toLowerCase(Locale.ROOT) +
                variableName.substring(1) +
                " in row " +
                rowNumber;
    }
}
