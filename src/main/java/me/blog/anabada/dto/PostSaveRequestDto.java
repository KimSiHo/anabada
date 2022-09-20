package me.blog.anabada.dto;

import lombok.Builder;
import lombok.Data;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Post;

@Builder
@Data
public class PostSaveRequestDto {

    private String title;
    private String content;
    private Account writer;

    public Post toEntity() {
        return Post.builder()
            .title(title)
            .content(content)
            .writer(writer)
            .build();
    }
}
