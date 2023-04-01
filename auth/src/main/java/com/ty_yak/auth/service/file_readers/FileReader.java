package com.ty_yak.auth.service.file_readers;

import com.ty_yak.auth.model.entity.User;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public abstract class FileReader {

    public abstract List<User> readUsers(MultipartFile file) throws IOException, Docx4JException;
}
