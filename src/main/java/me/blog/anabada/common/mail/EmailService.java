package me.blog.anabada.common.mail;

import me.blog.anabada.common.mail.dto.EmailMessage;

public interface EmailService {

    void sendEmail(EmailMessage emailMessage);
}
