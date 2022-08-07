package me.blog.anabada;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestConstructor(autowireMode = AutowireMode.ALL)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/application-local.yml")
@SpringBootTest
class AuthApplicationTests {

    @Test
    void contextLoads() {
    }
}
