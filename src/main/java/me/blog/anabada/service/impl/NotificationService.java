package me.blog.anabada.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.common.enums.NotificationStatus;
import me.blog.anabada.dao.NotificationRepository;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Notification;
import me.blog.anabada.entities.Product;

@RequiredArgsConstructor
@Transactional
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void handleSellNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).get();
        Product product = notification.getProduct();
        Account sender = notification.getSender();
        Account recipient = notification.getRecipient();

        sendNotification(recipient, sender, product, NotificationStatus.saleCompleted, "구매 확정되었습니다.");

        notificationRepository.delete(notification);
    }

    public void sendNotification(Account sender, Account recipient, Product product, NotificationStatus notificationStatus, String message) {
        Notification notification = Notification.builder()
            .message(message)
            .sender(sender)
            .product(product)
            .recipient(recipient)
            .notificationStatus(notificationStatus)
            .build();

        notificationRepository.save(notification);
    }
}




