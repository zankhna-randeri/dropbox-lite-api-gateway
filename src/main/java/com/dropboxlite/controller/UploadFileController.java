package com.dropboxlite.controller;

import com.dropboxlite.dao.FileDao;
import com.dropboxlite.model.FileInfo;
import com.dropboxlite.model.UploadFileOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UploadFileController {

  @Autowired
  private FileDao fileDao;

  @PostMapping(value = "/upload", consumes = "multipart/form-data")
  public UploadFileOutput upload(@RequestHeader(value = "userid") int userid,
                                 @RequestParam("file") MultipartFile input,
                                 @RequestParam("description") String description) {
    // 1. get file stream
    // 2. validate all values
    // 3. upload to s3
    // 4. update database entry
    // 5. return success else failure

    //TODO : OriginalFileName() doesn not work on Opera.
    String fileName = input.getOriginalFilename();
    String s3Key = userid + "/" + fileName;
    long uploadTimeStamp = System.currentTimeMillis();
    long updateTimeStamp = System.currentTimeMillis();

    FileInfo fileInfo = FileInfo.builder()
        .userId(userid)
        .fileName(fileName)
        .s3Key(s3Key)
        .fileCreationTimestamp(uploadTimeStamp)
        .fileUpdateTimestamp(updateTimeStamp)
        .description(description)
        .fileSize(input.getSize())
        .build();

    try {
      if (fileDao.uploadFile(input.getInputStream(), fileInfo)) {
        fileDao.insertFile(fileInfo);
        return UploadFileOutput.builder()
            .result(true)
            .build();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return UploadFileOutput.builder()
        .result(false)
        .build();
  }
}
