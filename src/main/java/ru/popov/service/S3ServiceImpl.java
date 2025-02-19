package ru.popov.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.popov.config.S3Config;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@AllArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service{

    @Autowired
    private S3Config s3Config;

    @Autowired
    private S3Client s3Client;


    @Override
    public void uploadFile(MultipartFile file) {
        var fileName = file.getOriginalFilename();
        var putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Config.getBucketName())
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        try(var stream = file.getInputStream()){
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(stream, file.getSize()));
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }
    }

    @Override
    public void downloadFile(String fileName) throws IOException {
        var downloadsDir = System.getProperty("user.home") + "/Downloads/";
        byte [] file = s3Client.getObjectAsBytes(builder -> builder
                        .bucket(s3Config.getBucketName())
                        .key(fileName))
                .asByteArray();
        var newFile = new File(downloadsDir + fileName);
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            fos.write(file);
        } catch (IOException e) {
        log.error(e.getLocalizedMessage());
        }
    }
}

