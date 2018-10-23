package com.dropboxlite.dao;

import com.dropboxlite.model.User;

public interface UserDao {
  /**
   * determine user already exist in database
   *
   * @param userEmail email of user
   * @return true if user exist in database else false
   */
  boolean isUserExist(String userEmail);

  /**
   * Insert user in database
   *
   * @param user user metadata
   * @return id of newly registered user
   */
  int registerUser(User user);

  /**
   * Check whether user can login or not
   *
   * @param userEmail email of user
   * @param password  password of user
   * @return User object if user provides correct logon credentials
   */
  User loginUser(String userEmail, String password);
}
