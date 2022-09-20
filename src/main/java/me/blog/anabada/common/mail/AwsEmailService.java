package me.blog.anabada.common.mail;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.anabada.common.mail.dto.EmailSenderDto;

@Slf4j
@Profile("prod")
@Component
@RequiredArgsConstructor
public class AwsEmailService implements EmailService{

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Override
    public Boolean send(final String subject, final String content, final List<String> receivers) {
        final EmailSenderDto senderDto = EmailSenderDto.builder()
            .to(receivers)
            .subject(subject)
            .content(content)
            .build();

        final SendEmailResult sendEmailResult = amazonSimpleEmailService.sendEmail(senderDto.toSendRequestDto());

        sendingResultMustSuccess(sendEmailResult);
        return true;
    }

    private void sendingResultMustSuccess(final SendEmailResult sendEmailResult) {
        if (sendEmailResult.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            log.error("{}", sendEmailResult.getSdkResponseMetadata().toString());
        }
    }
}