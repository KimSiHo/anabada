package me.blog.anabada.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import me.blog.anabada.entities.ProductImageFile;

@Transactional(readOnly = true)
public interface ProductImageFileRepository extends JpaRepository<ProductImageFile, Long> {

}
