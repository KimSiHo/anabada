package me.blog.anabada.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.anabada.dao.PostRepository;
import me.blog.anabada.dto.PostUpdateForm;
import me.blog.anabada.entities.Post;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BbsService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<Post> getPostList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        return postRepository.findWithAccount(pageable);
    }

    public Post updatePost(Long id, PostUpdateForm form) {
        Post post = postRepository.findById(id).get();
        post.update(form.getTitle(), form.getContent());

        return post;
    }
}
