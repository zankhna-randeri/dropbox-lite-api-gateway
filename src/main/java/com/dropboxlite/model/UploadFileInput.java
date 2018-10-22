package com.dropboxlite.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@ToString
public class UploadFileInput {
  private MultipartFile fileInput;
  private String folderName;
}