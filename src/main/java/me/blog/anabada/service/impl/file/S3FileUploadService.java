package me.blog.anabada.service.impl.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.anabada.common.exception.AnabadaServiceException;
import me.blog.anabada.dto.file.S3Component;
import me.blog.anabada.dto.file.UploadFile;
import me.blog.anabada.service.FileUploadService;

@Profile("fs-s3")
@Slf4j
@RequiredArgsConstructor
@Service
public class S3FileUploadService implements FileUploadService {

    private final AmazonS3Client amazonS3Client;
    private final S3Component s3Component;

    @Override
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }

        return  storeFileResult;
    }

    @Override
    public UploadFile storeFile(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());
        try {
            amazonS3Client.putObject(new PutObjectRequest(s3Component.getBucket(), s3Component.getDir() + storeFileName, multipartFile.getInputStream(), objectMetadata)
                                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new AnabadaServiceException("파일 저장 실패", e);
        }

        String url = amazonS3Client.getUrl(s3Component.getBucket(), s3Component.getDir() + storeFileName).toString();
        return UploadFile.builder()
            .url(url)
            .originalFilename(originalFilename)
            .storeFileName(storeFileName)
            .build();
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
