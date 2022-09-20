package me.blog.anabada.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.anabada.controller.common.CurrentUser;
import me.blog.anabada.dao.NotificationRepository;
import me.blog.anabada.entities.Account;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model, HttpSession session) {
        if (account != null) {
            model.addAttribute(account);

            Long notiCount = notificationRepository.countNotiByRecipient(account);
            model.addAttribute("notiCount", notiCount);
            session.setAttribute("notiCount", notiCount);
        }

        log.debug("enter to home");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}