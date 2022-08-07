package me.blog.anabada.service;

import org.springframework.web.multipart.MultipartFile;

import me.blog.anabada.dto.file.UploadInfo;

public interface FileUploadService {
    UploadInfo saveFile(MultipartFile file);
}
