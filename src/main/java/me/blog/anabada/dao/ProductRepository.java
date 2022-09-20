package me.blog.anabada.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import me.blog.anabada.common.enums.ProductKindCode;
import me.blog.anabada.entities.Product;

@Transactional(readOnly = true)
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p join fetch p.account where p.id=:id")
    Optional<Product> findWithAccount(@Param("id") Long id);

    @Query("select distinct p from Product p join fetch p.productImageFiles join fetch p.account where p.id=:id")
    Optional<Product> findOneProduct(@Param("id") Long id);

    @Query(value = "select p from Product p join fetch p.account a where p.productKindCode=:productKind and p.productTitle like concat('%', :keyword, '%')",
            countQuery = "select count(p) from Product p where p.productKindCode=:productKind and p.productTitle like concat('%', :keyword, '%')")
    Page<Product> findTop10ByProductKindAndSearch(Pageable pageable, @Param("keyword") String keyword, @Param("productKind") ProductKindCode productKindCode);

    @Query(value = "select p from Product p join fetch p.account where p.productKindCode=:productKind",
            countQuery = "select count(p) from Product p where p.productKindCode=:productKind")
    Page<Product> findTop10ByProductKind(Pageable pageable, @Param("productKind") ProductKindCode productKindCode);
}
