package me.blog.anabada.controller;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.anabada.common.validator.PasswordFormValidator;
import me.blog.anabada.controller.common.CurrentUser;
import me.blog.anabada.dao.NotificationRepository;
import me.blog.anabada.dto.Profile;
import me.blog.anabada.controller.form.PasswordForm;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Notification;
import me.blog.anabada.entities.ProductImageFile;
import me.blog.anabada.service.impl.AccountServiceImpl;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/settings")
@Controller
public class SettingsController {

    static final String SETTINGS_PROFILE_URL = "/profile";
    static final String SETTINGS_PASSWORD_URL = "/password";
    static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";
    static final String SETTINGS_PASSWORD_VIEW_NAME = "settings/password";

    private final AccountServiceImpl accountServiceImpl;
    private final NotificationRepository notificationRepository;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @Transactional
    @GetMapping("/notifications")
    public String settingsNotification(@CurrentUser Account account, Model model) {
        List<Notification> notifications = notificationRepository.findNotiByRecipient(account);
        if(notifications.size() > 0) {
            ProductImageFile productImageFile = notifications.get(0).getProduct().getProductImageFiles().get(0);
        }

        model.addAttribute("message", "알림 메시지");
        model.addAttribute("notifications", notifications);

        return "settings/notification";
    }

    @GetMapping(SETTINGS_PROFILE_URL)
    public String updateProfileForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new Profile(account));
        return SETTINGS_PROFILE_VIEW_NAME;
    }

    @PostMapping(SETTINGS_PROFILE_URL)
    public String updateProfile(@CurrentUser Account account, @Valid Profile profile, Errors errors, Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PROFILE_VIEW_NAME;
        }

        accountServiceImpl.updateProfile(account, profile);
        attributes.addFlashAttribute("message", "프로필을 수정했습니다.");
        return "redirect:/settings" + SETTINGS_PROFILE_URL;
    }

    @GetMapping(SETTINGS_PASSWORD_URL)
    public String updatePasswordForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return SETTINGS_PASSWORD_VIEW_NAME;
    }

    @PostMapping(SETTINGS_PASSWORD_URL)
    public String updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors,
        Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PASSWORD_VIEW_NAME;
        }

        accountServiceImpl.updatePassword(account, passwordForm.getNewPassword());
        attributes.addFlashAttribute("message", "패스워드를 변경했습니다.");
        return "redirect:/settings" + SETTINGS_PASSWORD_URL;
    }
}
