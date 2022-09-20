package me.blog.anabada.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 게시글 상태 코드
 * Main Code : PTC
 */
@Getter
@AllArgsConstructor
public enum PostTypeCode implements CodeDescription {
    PTC001("일반 게시글");

    private final String description;
}
