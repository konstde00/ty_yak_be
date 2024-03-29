package com.ty_yak.auth.service;

import com.ty_yak.auth.model.entity.User;
import com.ty_yak.auth.service.file_readers.FileReader;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {

    S3Service s3Service;
    UserService userService;
    Map<String, FileReader> readers;

    public FileService(S3Service s3Service,
                       @Lazy UserService userService,
                       Map<String, FileReader> readers) {
        this.s3Service = s3Service;
        this.userService = userService;
        this.readers = readers;
    }

    public List<User> readUsers(MultipartFile file) throws IOException, Docx4JException {

        log.info("'readUsers' invoked with file - {}", file.getOriginalFilename());

        var extension = getFileExtension(file);
        if (!readers.containsKey(extension)) {
            throw new IllegalArgumentException("Unsupported file extension - " + extension);
        }
        var reader = readers.get(extension);
        var users = reader.readUsers(file);

        var emails = users.stream().map(User::getEmail).collect(Collectors.toList());
        userService.validateEmails(emails);

        userService.saveAll(users);

        log.info("'readUsers' returned - {}", users);

        return users;
    }

    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return "";
        }
        int extensionIndex = originalFilename.lastIndexOf('.');
        if (extensionIndex == -1) {
            return "";
        }
        return originalFilename.substring(extensionIndex + 1);
    }

    public String saveUserAvatar(Long userId, MultipartFile avatar) {

        log.info("'saveUserAvatar' invoked with user id - {}", userId);

        String path = "users/" + userId + "/" + avatar.getOriginalFilename();
        var url = s3Service.uploadPublic(path, avatar);

        log.info("'saveUserAvatar' returned - {}", url);

        return url;
    }

    public void deleteByUrl(String url) {

        log.info("'deleteByUrl' invoked with url - {}", url);

        s3Service.delete(url);

        log.info("'deleteByUrl' returned 'Success'");
    }
}
