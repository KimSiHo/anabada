package me.blog.anabada.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 알림 메시지 상태 코드
 * Main Code : NSC
 */
@Getter
@AllArgsConstructor
public enum NotiStatusCode implements CodeDescription {
    NSC001("처리 미완료"),
    NSC002("처리 완료");

    private final String description;
}
