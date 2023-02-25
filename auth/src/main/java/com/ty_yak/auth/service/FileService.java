package com.ty_yak.auth.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {

    S3Service s3Service;

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
