package me.blog.anabada.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.boot.archive.scan.spi.AbstractScannerImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.controller.common.CurrentUser;
import me.blog.anabada.controller.form.LoginForm;
import me.blog.anabada.dao.AccountRepository;
import me.blog.anabada.dao.NotificationRepository;
import me.blog.anabada.entities.Account;
import me.blog.anabada.service.impl.AccountServiceImpl;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;
    private final AccountServiceImpl accountService;

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model, HttpSession session) {
        if (account != null) {
            model.addAttribute(account);

            Long notiCount = notificationRepository.countRecvMsgByRecipient(account);
            model.addAttribute("notiCount", notiCount);
            session.setAttribute("notiCount", notiCount);
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login2")
    public String login(@Valid LoginForm loginForm, Errors errors) {
        Account byNickname = accountRepository.findByNickname(loginForm.getUsername());
        accountService.login(byNickname);

        return "redirect:/";
    }
}