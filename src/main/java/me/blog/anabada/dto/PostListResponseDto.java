package me.blog.anabada.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Post;

@Getter
public class PostListResponseDto {

    private Long id;
    private String title;
    private Account writer;
    private LocalDateTime modifiedDate;

    public PostListResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.writer = post.getWriter();
        this.modifiedDate = post.getModifiedDate();
    }
}
