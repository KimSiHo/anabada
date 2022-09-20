package me.blog.anabada.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 구매 상태 코드
 * Main Code : PRS
 */
@Getter
@AllArgsConstructor
public enum PurchaseStatusCode implements CodeDescription {
    PRS001("구매 요청"),
    PRS002("구매 완료");

    private final String description;
}
