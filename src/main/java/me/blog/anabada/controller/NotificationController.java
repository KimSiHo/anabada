package me.blog.anabada.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.controller.common.CurrentUser;
import me.blog.anabada.dao.NotificationRepository;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Notification;

@RequiredArgsConstructor
@Controller
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/settings/notifications")
    public String notifications(@CurrentUser Account account, HttpSession session, Model model) {
        List<Notification> notifications = notificationRepository.findRecvMsgByRecipient(account);
        model.addAttribute("notifications", notifications);
        model.addAttribute("notiCount", notifications.size());

        return "settings/notification";
    }
}
