package me.blog.anabada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ConfigurationPropertiesScan(basePackageClasses = AnabadaApplication.class)
@SpringBootApplication
public class AnabadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnabadaApplication.class, args);
    }
}
