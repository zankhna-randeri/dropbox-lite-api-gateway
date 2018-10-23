package com.dropboxlite.controller;

import com.dropboxlite.dao.FileDao;
import com.dropboxlite.exception.InvalidRequestException;
import com.dropboxlite.model.ListFileOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListFileController {

  @Autowired
  private FileDao fileDao;

  @GetMapping("/listfile")
  public ListFileOutput listFiles(@RequestHeader(value = "userid") int userId) {

    if (userId <= 0) {
      throw new InvalidRequestException("UserId must be greater than 0");
    }
    return ListFileOutput.builder().files(fileDao.listFiles(userId)).build();
  }

  @GetMapping("/listfileAdmin")
  public ListFileOutput listAllUserFiles() {

    return ListFileOutput.builder().files(fileDao.listAllUserFiles()).build();
  }

}
