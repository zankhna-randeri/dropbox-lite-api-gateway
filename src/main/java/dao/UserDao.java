package dao;

import model.User;

public interface UserDao {
  /**
   * determine user already exist in database
   * @param userEmail email of user
   * @return true if user exist in database else false
   */
  boolean isUserExist(String userEmail);

  /**
   * Insert user in database
   * @param user user metadata
   * @return id of newly registered user
   */
  int registerUser(User user);
}
