package com.dropboxlite.controller;

import com.dropboxlite.dao.UserDao;
import com.dropboxlite.exception.InvalidRequestException;
import com.dropboxlite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  @Autowired
  private UserDao userDao;

  @GetMapping("/login")
  public User loginUser(@RequestHeader("email") String email,
                        @RequestHeader("password") String password) {

    if (email == null || email.trim().isEmpty()) {
      throw new InvalidRequestException("Email can not be empty");
    }
    if (password == null || password.trim().isEmpty()) {
      throw new InvalidRequestException("Password can not be empty");
    }
    return userDao.loginUser(email, password);
  }
}
