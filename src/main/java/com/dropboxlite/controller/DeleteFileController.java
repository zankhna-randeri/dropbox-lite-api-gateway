package com.dropboxlite.controller;

import com.dropboxlite.dao.FileDao;
import com.dropboxlite.exception.InvalidRequestException;
import com.dropboxlite.model.DeleteFileOutput;
import com.dropboxlite.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class DeleteFileController {

  @Autowired
  private FileDao fileDao;

  @DeleteMapping("/delete/{userId:[0-9]+}/{fileName:.+}")
  public DeleteFileOutput deleteFile(@PathVariable int userId,
                                     @PathVariable String fileName) {

    if (userId <= 0) {
      throw new InvalidRequestException("UserId must be greater than 0");
    }
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new InvalidRequestException("Filename can not be empty");
    }


    FileInfo fileInfo = null;
    try {
      fileInfo = fileDao.getFileInfo(userId, fileName);
      fileDao.deleteFileFromS3(fileInfo.getS3Key());
      fileDao.deleteFileFromDB(userId, fileName);
    } catch (FileNotFoundException e) {
      throw new InvalidRequestException("File not found");
    }
    return DeleteFileOutput.builder().status("success").build();
  }

}
