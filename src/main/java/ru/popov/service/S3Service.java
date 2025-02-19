package ru.popov.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "S3Service", description = "Сервис для работы с S3 хранилищем")
public interface S3Service {
    @Operation(summary = "Метод обработки загрузки файла в S3 хранилище")
    void uploadFile(MultipartFile file);
    void downloadFile(String fileName) throws IOException;
}
