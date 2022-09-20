package me.blog.anabada.dto.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Profile("fs-local")
@ConfigurationProperties(prefix = "local.filesystem")
@Component
public class LocalUploadComponent {

    private String uploadPath;
}
