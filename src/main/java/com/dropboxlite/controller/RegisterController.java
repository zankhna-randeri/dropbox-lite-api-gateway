package com.dropboxlite.controller;

import com.dropboxlite.dao.UserDao;
import com.dropboxlite.exception.InvalidRequestException;
import com.dropboxlite.model.RegisterOutput;
import com.dropboxlite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

  @Autowired
  private UserDao userDao;

  @PostMapping(value = "/user", consumes = {"application/json"})
  public RegisterOutput createUser(@RequestBody User user) {

    //1. check user already registered
    //2. create folder in s3
    //3. insert user in rds mysql

    if (user == null) {
      throw new InvalidRequestException("User can not be null");
    }
    if (user.getUserEmail() == null || user.getUserEmail().trim().isEmpty()) {
      throw new InvalidRequestException("User email can not be empty");
    }

    if (userDao.isUserExist(user.getUserEmail())) {
      User dbUser = userDao.getUserInfoByEmail(user.getUserEmail());
      return RegisterOutput.builder()
          .userId(dbUser.getUserId())
          .userCreated(false)
          .build();
    } else {
      int userId = userDao.registerUser(user);
      return RegisterOutput.builder()
          .userCreated(true)
          .userId(userId)
          .build();
    }

  }
}
