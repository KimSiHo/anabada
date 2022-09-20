package me.blog.anabada.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 물건 상태 코드
 * Main Code : PSC
 */
@Getter
@AllArgsConstructor
public enum ProductStatusCode implements CodeDescription{
    PSC001("판매중"),
    PSC002("판매 완료");

    private final String description;
}
