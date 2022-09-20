package me.blog.anabada.dto.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Data
@Profile("fs-s3")
@ConfigurationProperties(prefix = "cloud.aws.s3")
@Component
public class S3Component {

    private String bucket;
    private String dir;
}
