package me.blog.anabada.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.BatchSize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.blog.anabada.common.enums.ProductKindCode;
import me.blog.anabada.common.enums.ProductStatusCode;
import me.blog.anabada.entities.audit.BaseTimeEntity;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String bio;

    private int price;

    private String productTitle;

    @Enumerated(EnumType.STRING)
    private ProductKindCode productKindCode;

    @Enumerated(value = EnumType.STRING)
    private ProductStatusCode productStatusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "product")
    private List<ProductImageFile> productImageFiles = new ArrayList<>();

    public void soldOut() {
        this.productStatusCode = ProductStatusCode.PSC002;
    }
}
