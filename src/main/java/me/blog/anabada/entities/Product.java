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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.blog.anabada.common.enums.ProductKind;
import me.blog.anabada.common.enums.ProductStatus;

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

    // todo 상품 관련 게시판으로 refactor 필요
    private String productTitle;

    @Enumerated(EnumType.STRING)
    private ProductKind productKind;

    private String fileName;

    @Enumerated(value = EnumType.STRING)
    private ProductStatus productStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @OneToMany(mappedBy = "product")
    private List<ProductImageFile> productImageFile = new ArrayList<>();

    public void soldOut() {
        this.productStatus = ProductStatus.soldOut;
    }
}
