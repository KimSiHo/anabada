package me.blog.anabada.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import me.blog.anabada.dto.file.UploadFile;

public interface FileUploadService {
    List<UploadFile> storeFiles(List<MultipartFile> file);

    UploadFile storeFile(MultipartFile file);
}
