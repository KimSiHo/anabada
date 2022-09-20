package me.blog.anabada.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.Fetch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Notification;

@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Long countNotiByRecipient(Account recipient);

    @Query("select n from Notification n join fetch n.sender join fetch n.product where n.recipient=:recipient")
    List<Notification> findNotiByRecipient(@Param("recipient")Account recipient);

    @EntityGraph(attributePaths = {"product"})
    Notification findWithProductById(Long notificationId);

    @EntityGraph(attributePaths = {"product", "sender", "recipient"})
    Optional<Notification> findAllWithById(Long notificationId);
}
