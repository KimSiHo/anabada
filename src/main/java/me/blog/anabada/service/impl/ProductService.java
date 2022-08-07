package me.blog.anabada.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.common.enums.ProductKind;
import me.blog.anabada.common.enums.ProductStatus;
import me.blog.anabada.controller.form.ProductUploadForm;
import me.blog.anabada.dao.ProductImageFileRepository;
import me.blog.anabada.dao.ProductRepository;
import me.blog.anabada.dto.file.UploadInfo;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Product;
import me.blog.anabada.entities.ProductImageFile;
import me.blog.anabada.service.FileUploadService;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageFileRepository productImageFileRepository;

    private final FileUploadService fileUploadService;

    public Product upload(MultipartFile file, Account account, ProductUploadForm productUploadForm) {
        UploadInfo uploadInfo = fileUploadService.saveFile(file);

        Product product = Product.builder()
            .bio(productUploadForm.getBio())
            .account(account)
            .price(productUploadForm.getPrice())
            .productTitle(productUploadForm.getProductTitle())
            .fileName(file.getOriginalFilename())
            .productKind(productUploadForm.getProductKind())
            .productStatus(ProductStatus.onSale)
            .build();

        ProductImageFile productImageFile = ProductImageFile.builder()
            .product(product)
            .imageURL(uploadInfo.getPath())
            .product(product)
            .build();

        productImageFileRepository.save(productImageFile);
        return productRepository.save(product);
    }

    public void sell(Long productId) {
        Product product = productRepository.findById(productId).get();
        product.soldOut();
    }

    public Page<Product> findTop10ByProductKind(Pageable pageable, ProductKind enumProductKind) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "id"));

        return productRepository.findTop10ByProductKind(pageable, enumProductKind);
    }

    public Page<Product> findProductSearchList(Pageable pageable, String keyword, String productKind) {
        ProductKind enumProductKind = ProductKind.valueOf(productKind);
        pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        return productRepository.findTop10SearchProductList(pageable, keyword, enumProductKind);
    }
}
