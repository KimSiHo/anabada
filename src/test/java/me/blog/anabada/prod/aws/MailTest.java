package me.blog.anabada.prod.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.common.mail.AwsEmailService;
import me.blog.anabada.common.mail.EmailService;
import me.blog.anabada.prod.src.TestConst;

@RequiredArgsConstructor
@ActiveProfiles("prod")
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest
public class MailTest {

    private final EmailService emailService;

    @Test
    public void sendEmailTest() {
        assertNotNull(emailService);
        assertEquals(emailService.getClass(), AwsEmailService.class, "aws 이메일 서비스 사용");

        Boolean result = emailService.send("test 실행", "메일 정상 작동 여부 테스트 ", List.of(TestConst.TEST_EAMIL));
        assertEquals(true, result, "메일 정상 작동 실패");
    }
}
