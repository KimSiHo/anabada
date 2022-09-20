package me.blog.anabada.dto.file;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadFile {
    private String url;
    private String storeFileName;
    private String originalFilename;
}
