package me.blog.anabada.common.mail;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({"local"})
@Component
public class ConsoleEmailService implements EmailService {

    @Override
    public Boolean send(String subject, String content, List<String> receivers) {
        log.info("sent email: {}", content);
        return true;
    }
}
