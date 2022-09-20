package me.blog.anabada.prod.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ActiveProfiles("prod")
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest
public class CommonTest {

    private final Environment environment;

    @DisplayName("prod 프로파일 일치하는지 테스트")
    @Test
    void envTest() {
        final String[] activeProfiles = environment.getActiveProfiles();
        log.debug(Arrays.toString(activeProfiles));

        assertThat(activeProfiles).containsOnly("prod", "fs-s3", "credentials");
    }
}
