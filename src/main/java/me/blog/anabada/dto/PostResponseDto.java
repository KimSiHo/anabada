package me.blog.anabada.dto;

import lombok.Data;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Post;

@Data
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private Account writer;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getWriter();
    }
}
