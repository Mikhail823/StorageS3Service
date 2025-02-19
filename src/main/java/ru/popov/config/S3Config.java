package ru.popov.config;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3Config {
    @Value("${cloud.s3.accessKeyId}")
    private String accessKeyId;

    @Value("${cloud.s3.secretKey}")
    private String secretKey;

    @Value("${cloud.s3.region}")
    private String region;

    @Value("${cloud.s3.endpoint}")
    private String endpoint;

    @Value("${cloud.s3.bucket-name}")
    @Getter
    private String bucketName;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create(accessKeyId, secretKey)))
                .region(Region.of(region))
                .build();
    }
}
