package me.blog.anabada.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import me.blog.anabada.entities.Post;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select p from Post p join fetch p.writer",
        countQuery = "select count(p) from Post p")
    Page<Post> findWithAccount(Pageable pageable);

    @EntityGraph(attributePaths = {"writer"})
    @Query("select p from Post p where p.id=:id")
    Optional<Post> findOneWithAccount(Long id);
}
