package com.ty_yak.auth.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ty_yak.auth.model.dto.file.FileDownloadDto;
import com.ty_yak.auth.model.entity.File;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead;


@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class S3Service {

    @NonFinal
    @Value("${s3.bucket-name}")
    String bucketName;

    @NonFinal
    @Value("${s3.folders.avatars}")
    String doSpaceEndpoint;

    AmazonS3 s3Client;

    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @SneakyThrows(IOException.class)
    public String uploadPublic(String filePath, MultipartFile multipartFile) {

        log.info("'uploadPublic' invoked with path - {} and file - {} ", filePath, multipartFile);

        var metadata = createMetadata(multipartFile);

        s3Client.putObject(new PutObjectRequest(bucketName, filePath, multipartFile.getInputStream(), metadata)
                .withCannedAcl(PublicRead));

        var OUTER_URL = "https://" + bucketName + "." + doSpaceEndpoint + "/";
        var url = OUTER_URL + filePath;

        log.info("'uploadPublic' returned - {} ", url);

        return url;
    }

    public FileDownloadDto download(File file) {

        log.info("download' invoked with url - {}", file.getUrl());
        var s3Object = s3Client.getObject(new GetObjectRequest(bucketName, getPathInSpace(file.getUrl())));

        var fileDto = FileDownloadDto
                .builder()
                .fileName(file.getName())
                .contentType(s3Object.getObjectMetadata().getContentType())
                .contentLength(s3Object.getObjectMetadata().getContentLength())
                .data(new InputStreamResource(s3Object.getObjectContent()))
                .build();

        log.info("'download' returned 'Success'");

        return fileDto;
    }

    public void delete(String url) {

        log.info("'delete' invoked with url - {}", url);
        s3Client.deleteObject(bucketName, getPathInSpace(url));
        log.info("'delete' returned 'Success'");
    }

    private String getPathInSpace(String url) {

        log.info("'getPathInSpace' invoked with url - {}", url);

        var OUTER_URL = "https://" + bucketName + "/" + doSpaceEndpoint + "/";
        var filePathInSpace = url.replace(OUTER_URL, "");

        log.info("'getPathInSpace' returned - {}", filePathInSpace);

        return filePathInSpace;
    }

    @SneakyThrows(IOException.class)
    private ObjectMetadata createMetadata(MultipartFile multipartFile) {

        log.info("'createMetadata' invoked");

        var metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getInputStream().available());

        if (multipartFile.getContentType() != null && !"".equals(multipartFile.getContentType())) {
            metadata.setContentType(multipartFile.getContentType());
        }

        log.info("'createMetadata' returned 'Success'");

        return metadata;
    }
}
