package me.blog.anabada.common.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.controller.form.ProductUploadForm;

@RequiredArgsConstructor
@Component
public class ProductUploadFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(ProductUploadForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ProductUploadForm productUploadForm = (ProductUploadForm) object;
        List<MultipartFile> multipartFileList = productUploadForm.getProductImageFiles();

        if (multipartFileList.size() == 0) {
            errors.rejectValue("productImageFile", "invalid.productImageFile", new Object[]{productUploadForm.getProductImageFiles()}, "파일을 첨부해주세요.");
        }
    }
}
