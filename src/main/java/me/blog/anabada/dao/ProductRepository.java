package me.blog.anabada.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import me.blog.anabada.common.enums.ProductKind;
import me.blog.anabada.common.enums.ProductStatus;
import me.blog.anabada.entities.Product;

@Transactional(readOnly = true)
public interface ProductRepository extends JpaRepository<Product, Long> {

    Long countProductByProductStatus(ProductStatus productStatus);

    List<Product> findTop8ByProductStatusAndProductKind(ProductStatus onSale, ProductKind cloth);

    @Query("select p from Product p where p.productKind =:productKind and p.productTitle like concat('%', :keyword, '%')")
    Page<Product> findTop10SearchProductList(Pageable pageable, @Param("keyword") String keyword, @Param("productKind") ProductKind productKind);

    Page<Product> findTop10ByProductKind(Pageable pageable, ProductKind productKind);
}
