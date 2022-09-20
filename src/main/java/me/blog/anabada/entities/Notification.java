package me.blog.anabada.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.blog.anabada.common.enums.NotiStatusCode;
import me.blog.anabada.common.enums.PurchaseStatusCode;
import me.blog.anabada.entities.audit.BaseTimeEntity;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Account sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Account recipient;

    private String message;

    private Boolean isDelete;

    @Enumerated(value = EnumType.STRING)
    private NotiStatusCode notiStatusCode;

    @Enumerated(value = EnumType.STRING)
    private PurchaseStatusCode purchaseStatusCode;

    public void processDone() {
        this.notiStatusCode = NotiStatusCode.NSC002;
    }
}
