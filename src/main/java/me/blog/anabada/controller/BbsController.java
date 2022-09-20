package me.blog.anabada.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.anabada.common.exception.AnabadaServiceException;
import me.blog.anabada.controller.common.CurrentUser;
import me.blog.anabada.dto.PostResponseDto;
import me.blog.anabada.dto.PostSaveForm;
import me.blog.anabada.dto.PostUpdateForm;
import me.blog.anabada.dao.PostRepository;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Post;
import me.blog.anabada.service.impl.BbsService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bbs")
@Controller
public class BbsController {

    private final PostRepository postRepository;
    private final BbsService bbsService;

    @GetMapping("/list")
    public String index(@PageableDefault Pageable pageable, Model model) {
        Page<Post> postList = bbsService.getPostList(pageable);

        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("postList", postList);

        log.debug("총 element 수 : {}, 전체 page 수 : {}, 페이지에 표시할 element 수 : {}, 현재 페이지 index : {}, 현재 페이지의 element 수 : {}",
            postList.getTotalElements(), postList.getTotalPages(), postList.getSize(), postList.getNumber(), postList.getNumberOfElements());

        return "bbs/index";
    }

    @GetMapping("/create")
    public String postCreate() {
        return "bbs/post-create";
    }

    @GetMapping("/update/{id}")
    public String postUpdate(@PathVariable Long id, Model model) {
        Post post = postRepository.findOneWithAccount(id).orElseThrow(() -> new AnabadaServiceException("not found post"));
        PostResponseDto postResponseDto = new PostResponseDto(post);
        model.addAttribute("post", postResponseDto);

        return "bbs/post-update";
    }

    @PostMapping("/create")
    public String save(@CurrentUser Account account, @ModelAttribute PostSaveForm form) {
        form.setWriter(account);
        Post post = form.toEntity();
        postRepository.save(post);

        return "redirect:/bbs/list";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute PostUpdateForm form) {
        Post post = bbsService.updatePost(id, form);
        log.debug("post = {}" + post.toString());

        return "redirect:/bbs/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        postRepository.deleteById(id);

        return "redirect:/bbs/list";
    }
}
