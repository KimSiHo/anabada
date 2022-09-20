package me.blog.anabada.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.common.enums.NotiStatusCode;
import me.blog.anabada.common.enums.PurchaseStatusCode;
import me.blog.anabada.common.exception.AnabadaServiceException;
import me.blog.anabada.dao.NotificationRepository;
import me.blog.anabada.dao.ProductRepository;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Notification;
import me.blog.anabada.entities.Product;

@RequiredArgsConstructor
@Transactional
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProductRepository productRepository;

    public void sendPurchaseNoti(Account buyUser, Long id) {
        Product product = productRepository.findWithAccount(id).orElseThrow(() -> new AnabadaServiceException("not found product"));

        sendNoti(buyUser, product.getAccount(), product, PurchaseStatusCode.PRS001, "구매 요청 보냅니다");
    }


    public void handleSellNoti(Long notificationId) {
        Notification noti = notificationRepository.findAllWithById(notificationId).orElseThrow(() -> new AnabadaServiceException("not found noti :" + notificationId));
        sendNoti(noti.getRecipient(), noti.getSender(), noti.getProduct(), PurchaseStatusCode.PRS002, "구매 확정되었습니다.");

        noti.processDone();
    }

    private void sendNoti(Account sender, Account recipient, Product product, PurchaseStatusCode purchaseStatusCode, String message) {
        Notification notification = Notification.builder()
            .message(message)
            .sender(sender)
            .product(product)
            .recipient(recipient)
            .notiStatusCode(NotiStatusCode.NSC001)
            .purchaseStatusCode(purchaseStatusCode)
            .build();

        notificationRepository.save(notification);
    }
}




