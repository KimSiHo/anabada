package me.blog.anabada.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import me.blog.anabada.entities.Post;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {

}
