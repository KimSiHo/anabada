package me.blog.anabada.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.common.enums.ProductKindCode;
import me.blog.anabada.common.enums.ProductStatusCode;
import me.blog.anabada.controller.form.ProductUploadForm;
import me.blog.anabada.dao.NotificationRepository;
import me.blog.anabada.dao.ProductImageFileRepository;
import me.blog.anabada.dao.ProductRepository;
import me.blog.anabada.dto.file.UploadFile;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Notification;
import me.blog.anabada.entities.Product;
import me.blog.anabada.entities.ProductImageFile;
import me.blog.anabada.service.FileUploadService;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageFileRepository productImageFileRepository;
    private final NotificationRepository notificationRepository;

    private final NotificationService notificationService;
    private final FileUploadService fileUploadService;

    public Boolean upload(Account account, ProductUploadForm form) {
        List<UploadFile> uploadFiles = fileUploadService.storeFiles(form.getProductImageFiles());

        Product product = Product.builder()
            .bio(form.getBio())
            .account(account)
            .price(form.getPrice())
            .productTitle(form.getProductTitle())
            .productKindCode(form.getProductKindCode())
            .productStatusCode(ProductStatusCode.PSC001)
            .build();
        productRepository.save(product);

        List<ProductImageFile> productImageFileList = new ArrayList<>();
        for (UploadFile uploadFile : uploadFiles) {
            ProductImageFile productImageFile = ProductImageFile.builder()
                .product(product)
                .url(uploadFile.getUrl())
                .originalFileName(uploadFile.getOriginalFilename())
                .storeFileName(uploadFile.getStoreFileName())
                .build();

            productImageFileList.add(productImageFile);
        }
        for (ProductImageFile productImageFile : productImageFileList) {
            productImageFile.changeProduct(product);
            productImageFileRepository.save(productImageFile);
        }

        return true;
    }

    public void sell(Long productId) {
        Product product = productRepository.findById(productId).get();
        product.soldOut();
    }

    public Page<Product> findTop10ByProductKind(Pageable pageable, ProductKindCode enumProductKindCode) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "id"));

        return productRepository.findTop10ByProductKind(pageable, enumProductKindCode);
    }

    public Page<Product> findProductSearchList(Pageable pageable, String keyword, String productKind) {
        //ProductKind enumProductKind = ProductKind.valueOf(productKind);
        //pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        //return productRepository.findTop10SearchProductList(pageable);
        //return productRepository.findTop10SearchProductList(pageable, keyword, enumProductKind);
        return null;
    }

    public void sellProductByNotiId(Long notificationId) {
        Notification notification = notificationRepository.findWithProductById(notificationId);
        notification.getProduct().soldOut();

        notificationService.handleSellNoti(notificationId);
    }
}
