package ru.popov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.popov.service.S3Service;
import java.io.IOException;

@RestController
@RequestMapping("/s3")
@Slf4j
@Tag(name = "S3Controller", description = "Контроллер обрабатывает загрузку и выгрузку файлов с S3 хранилища по REST API")
public class S3Controller {
    @Autowired
    private S3Service s3Service;

    @Operation(summary = "POST метод позволяет загружать файл в S3 хранилище",
            description = "В ответ возвращается строка String")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная загрузка файла",
            content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "403", description = "Failed to upload file: Invalid or unknown access key id",
            content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", description = "NoSuchBucket - Указанного бакета не существует")
    })
    @PostMapping("/upload")
    public String uploadFile(@RequestParam(name = "file")
                                 @Parameter(description = "Файл", example = "text.pdf") MultipartFile file) {
        try {
            s3Service.uploadFile(file);
            log.info("File " + file.getOriginalFilename() + " upload successfully!");
            return "File uploaded successfully!";
        } catch (Exception e) {
            log.error("Failed to upload file error: {}", e.getMessage());
            return "Failed to upload file: " + e.getMessage();
        }
    }

    @Operation(summary = "GET метод позволять производить загрузку файла с S3 хранилища на локальную машину",
    description = "В ответ возвращается массив байтов byte[]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная загрузка файла",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "403", description = "Указан не верный access key id",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Указанный файл не найден")
    })
    @GetMapping("/download")
    public String downloadFile(@RequestParam("key")
                                   @Parameter(description = "Название файла с расширением",
                                           example = "text.pdf") String key) {
        try {
            s3Service.downloadFile(key);
            log.info("File " + key + " download successfully!");
            return "File download successfully!";
        } catch (IOException e) {
            log.error("Failed to download file error: {}", e.getMessage());
            return "Failed to download file: " + e.getMessage();
        }
    }
}
