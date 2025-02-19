package ru.popov.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Info;

import java.util.List;
@Configuration
@AllArgsConstructor
public class OpenApiConfig {
    private Environment environment;

    @Bean
    public OpenAPI defineOpenAPI () {
        var server = new Server();
        String serverUrl = environment.getProperty("api.server.url");
        server.setUrl(serverUrl);
        server.setDescription("Development");

        var myContact = new Contact();
        myContact.setName("Mikhail Popov");
        myContact.setEmail("rabota822@bk.ru");
        var info = new Info()
                .title("Системное API для загрузки и выгрузки файлов в S3 хранилище")
                .version("1.0")
                .description("Это API предоставляет эндпоинты для загрузки и выгрузки файлов в S3 хранилище.")
                .contact(myContact);
        return new OpenAPI().info(info).servers(List.of(server));
    }
}
