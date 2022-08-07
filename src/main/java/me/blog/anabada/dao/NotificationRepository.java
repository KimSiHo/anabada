package me.blog.anabada.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Notification;

@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Long countRecvMsgByRecipient(Account recipient);

    List<Notification> findRecvMsgByRecipient(Account recipient);
}
