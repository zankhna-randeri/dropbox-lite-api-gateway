package com.dropboxlite.controller;

import com.dropboxlite.dao.FileDao;
import com.dropboxlite.exception.InvalidRequestException;
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
  public UploadFileOutput upload(@RequestHeader(value = "userid") String userid,
                                 @RequestParam("file") MultipartFile input,
                                 @RequestParam("description") String description) {
    // 1. get file stream
    // 2. validate all values
    // 3. upload to s3
    // 4. update database entry
    // 5. return success else failure

    //TODO: Test case for string UserID remaining
    if (userid == null || userid.trim().isEmpty()) {
      throw new InvalidRequestException("User Id can not be empty");
    }
    if (description == null || description.trim().isEmpty()) {
      throw new InvalidRequestException("File description can not be empty");
    }
    int userIntId = Integer.parseInt(userid);
    if (userIntId == 0) {
      throw new InvalidRequestException("UserId must be greater than 0");
    }

    //TODO : OriginalFileName() doesn not work on Opera.
    String fileName = input.getOriginalFilename();
    String s3Key = userid + "/" + fileName;
    long uploadTimeStamp = System.currentTimeMillis();
    long updateTimeStamp = System.currentTimeMillis();

    FileInfo fileInfo = FileInfo.builder()
        .userId(userIntId)
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
