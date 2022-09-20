package me.blog.anabada.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.anabada.common.enums.ProductKindCode;
import me.blog.anabada.common.exception.AnabadaServiceException;
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

    private final ProductUploadFormValidator productUploadFormValidator;
    private final ProductService productService;
    private final NotificationService notificationService;

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
    public String uploadProduct(@CurrentUser Account account, @Valid @ModelAttribute ProductUploadForm form, Errors errors) {
        if (errors.hasErrors()) {
            return "product/product-upload";
        }
        log.debug("productUploadForm = {}", form);
        log.debug("file = {}", form.getProductImageFiles());

        productService.upload(account, form);
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
    public String productList(@PathVariable(name = "productKind") String strProductKind, Model model, @PageableDefault Pageable pageable) {
        ProductKindCode productKindCode = ProductKindCode.findByStr(strProductKind);
        Page<Product> top10ByByProductKind = productService.findTop10ByProductKind(pageable, productKindCode);

        model.addAttribute("productList", top10ByByProductKind);
        model.addAttribute("productKind", strProductKind);

        return "product/product-list";
    }

    @GetMapping("/detail/{productKind}/{id}")
    public String detailElectronicProduct(@CurrentUser Account account, @PathVariable Long id, @PathVariable String productKind, Model model) {
        Product product = productRepository.findOneProduct(id)
            .orElseThrow(() -> new AnabadaServiceException("not found product :" + productKind + ", " + id));

        if(product.getAccount().getId() != account.getId()) {
            model.addAttribute("isSell", true);
        } else {
            model.addAttribute("isSell", false);
        }
        model.addAttribute("product", product);
        model.addAttribute("productKind", productKind);
        model.addAttribute("productImages", product.getProductImageFiles());

        return "product/product-detail";
    }

    @GetMapping("/request/buy/{id}")
    public String buyProduct(@CurrentUser Account user, @PathVariable Long id, Model model) {
        notificationService.sendPurchaseNoti(user, id);

        return "redirect:/";
    }

    @GetMapping("/response/sell")
    public String sellProduct(@RequestParam Long notificationId) {
        productService.sellProductByNotiId(notificationId);

        return "redirect:/";
    }

    @GetMapping("/response/reject")
    public String rejectProduct(@CurrentUser Account account,
        @RequestParam(value = "productId", required = false) Long productId,
        @RequestParam(value = "notificationId", required = false) Long notificationId,
        Model model) {
        notificationService.handleSellNoti(notificationId);
        productService.sell(productId);

        return "redirect:/";
    }
}
