package me.blog.anabada.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostUpdateRequestDto {
    private String title;
    private String content;
}
