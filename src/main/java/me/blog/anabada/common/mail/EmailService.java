package me.blog.anabada.common.mail;

import java.util.List;

public interface EmailService {

    Boolean send(String subject, String content, List<String> receivers);
}
