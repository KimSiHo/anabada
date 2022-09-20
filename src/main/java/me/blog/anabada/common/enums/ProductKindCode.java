package me.blog.anabada.common.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 물건 유형 코드
 * Main Code : PKC
 */
@Getter
@AllArgsConstructor
public enum ProductKindCode implements CodeDescription{
    PKC001("computer", "컴퓨터"),
    PKC002("electronic", "전자"),
    PKC003("furniture", "가구"),
    PKC004("cloth", "옷"),
    PKC005("shoes", "신발");

    private final String category;
    private final String description;

    public static ProductKindCode findByStr(String strProductKind) {
        Optional<ProductKindCode> findVal = Arrays.stream(values()).filter(productKindCode -> productKindCode.getCategory().equals(strProductKind))
                                                .findFirst();

        if(findVal.isPresent()) {
            return findVal.get();
        } else {
            return null;
        }
    }
}
