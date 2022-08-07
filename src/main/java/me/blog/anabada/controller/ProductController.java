package me.blog.anabada.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.anabada.common.enums.NotificationStatus;
import me.blog.anabada.common.enums.ProductKind;
import me.blog.anabada.common.validator.ProductUploadFormValidator;
import me.blog.anabada.controller.common.CurrentUser;
import me.blog.anabada.controller.form.ProductUploadForm;
import me.blog.anabada.dao.ProductRepository;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.Product;
import me.blog.anabada.service.impl.NotificationService;
import me.blog.anabada.service.impl.ProductService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/product")
@Controller
public class ProductController {

    private final ProductService productService;
    private final NotificationService notificationService;
    private final ProductUploadFormValidator productUploadFormValidator;

    private final ProductRepository productRepository;

    @InitBinder("productUploadForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(productUploadFormValidator);
    }

    @GetMapping("/upload")
    public String uploadProduct(Model model) {
        model.addAttribute(new ProductUploadForm());

        return "product/product-upload";
    }

    @PostMapping("/upload")
    public String uploadProduct(@Valid ProductUploadForm productUploadForm, Errors errors,
        @CurrentUser Account account, @RequestParam("productImageFile") MultipartFile file) {
        if (errors.hasErrors()) {
            return "product/product-upload";
        }
        log.debug("productUploadForm = {}", productUploadForm);
        log.debug("file = {}", file);

        productService.upload(file, account, productUploadForm);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam(name = "keyword") String keyword,
        @RequestParam(name = "productKind") String productKind,
        @PageableDefault Pageable pageable, Model model) {
        log.debug("검색 KEYWORD : {}, 검색 상품 종류 : {}", keyword, productKind);

        Page<Product> productSearchList = productService.findProductSearchList(pageable, keyword, productKind);

        model.addAttribute("productList", productSearchList);
        model.addAttribute("productKind", productKind);

        return "product/product-list";
    }

    @GetMapping("/list/{productKind}")
    public String productList(@PageableDefault Pageable pageable, @PathVariable String productKind, Model model) {
        ProductKind enumProductKind = ProductKind.valueOf(productKind);
        Page<Product> top10ByByProductKind = productService.findTop10ByProductKind(pageable, enumProductKind);

        model.addAttribute("productList", top10ByByProductKind);
        model.addAttribute("productKind", productKind);

        return "product/product-list";
    }

    @GetMapping("/detail/{productKind}/{id}")
    public String detailElectronicProduct(@PathVariable Long id, @PathVariable String productKind, Model model) {
        Optional<Product> byId = productRepository.findById(id);
        Product product = byId.get();
        model.addAttribute(product);
        model.addAttribute("productKind", productKind);
        model.addAttribute("productImages", product.getProductImageFile());

        return "product/product-detail";
    }

    @GetMapping("/buy/{id}")
    public String buyProduct(@CurrentUser Account buyer, @PathVariable Long id, Model model) {
        Optional<Product> byId = productRepository.findById(id);
        Product product = byId.get();
        Account owner = product.getAccount();

        notificationService.sendNotification(buyer, owner, product, NotificationStatus.purchaseRequest, "구매 요청입니다");

        return "redirect:/";
    }

    @GetMapping("/sell")
    public String buyProduct(@CurrentUser Account account,
        @RequestParam(value = "productId", required = false) Long productId,
        @RequestParam(value = "notificationId", required = false) Long notificationId,
        Model model) {
        notificationService.handleSellNotification(notificationId);
        productService.sell(productId);

        return "redirect:/";
    }
}
