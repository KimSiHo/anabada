package me.blog.anabada.service.impl;

import java.io.InputStream;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.anabada.dto.file.S3Component;
import me.blog.anabada.dto.file.UploadInfo;
import me.blog.anabada.service.FileUploadService;

@Profile("fs-s3")
@Slf4j
@RequiredArgsConstructor
@Service
public class S3FileUploadService implements FileUploadService {

    private final AmazonS3Client amazonS3Client;
    private final S3Component s3Component;

    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(s3Component.getBucket(), fileName, inputStream, objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String getFileUrl(String fileName) {
        return String.valueOf(amazonS3Client.getUrl(s3Component.getBucket(), fileName));
    }

    @Override
    public UploadInfo saveFile(MultipartFile file) {
        return null;
    }
}
