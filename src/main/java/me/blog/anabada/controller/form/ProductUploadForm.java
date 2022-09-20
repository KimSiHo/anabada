package me.blog.anabada.controller.form;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.blog.anabada.common.enums.ProductKindCode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductUploadForm {

    @NotBlank
    @Length(min = 8, max = 50)
    private String bio;

    @NotNull
    private ProductKindCode productKindCode;

    @NotNull
    private int price;

    @NotBlank
    @Length(min = 8, max = 30)
    private String productTitle;

    private List<MultipartFile> productImageFiles;
}
