package com.dropboxlite.dao;

import com.dropboxlite.model.User;
import com.dropboxlite.utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class UserDaoImpl implements UserDao, AutoCloseable {

  private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

  private static final String USER_EXIST_QUERY_FORMAT =
      "Select * from User where user_email='%s'";

  private static final String USER_INSERT_QUERY_FORMAT =
      "Insert into User (first_name,last_name,user_email,password)" + " " +
          "values (?,?,?,?)";

  private static final String USER_LOGIN_QUERY_FORMAT =
      "Select * from User where user_email='%s' and password='%s'";

  private Connection connection;

  public UserDaoImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public boolean isUserExist(String userEmail) {
    try {
      String query = String.format(USER_EXIST_QUERY_FORMAT, userEmail);
      return DatabaseUtils.isRecordExist(connection, query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int registerUser(User user) {
    try (PreparedStatement statement =
             connection.prepareStatement(USER_INSERT_QUERY_FORMAT, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, user.getFirstName());
      statement.setString(2, user.getLastName());
      statement.setString(3, user.getUserEmail());
      statement.setString(4, user.getPassword());
      int n = statement.executeUpdate();
      if (n != 1) {
        throw new IllegalStateException("Update entries are less than 1");
      }
      ResultSet result = statement.getGeneratedKeys();
      result.next();
      return result.getInt(1);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws Exception {
    connection.close();
  }

  @Override
  public User loginUser(String userEmail, String password) {
    String query = String.format(USER_LOGIN_QUERY_FORMAT, userEmail, password);
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      ResultSet resultSet = statement.executeQuery();
      User user = null;
      if (resultSet.next()) {
        user = new User();
        user.setUserId(resultSet.getInt(1));
        user.setFirstName(resultSet.getString(2));
        user.setLastName(resultSet.getString(3));
        user.setUserEmail(resultSet.getString(4));
        user.setPassword(resultSet.getString(5));
        user.setAdminUser(resultSet.getBoolean(6));

      }

      return user;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public User getUserInfoByEmail(String userEmail) {
    String query = String.format(USER_EXIST_QUERY_FORMAT, userEmail);
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      ResultSet resultSet = statement.executeQuery();
      User user = null;
      if (resultSet.next()) {
        user = new User();
        user.setUserId(resultSet.getInt(1));
        user.setFirstName(resultSet.getString(2));
        user.setLastName(resultSet.getString(3));
        user.setUserEmail(resultSet.getString(4));
        user.setPassword(resultSet.getString(5));
        user.setAdminUser(resultSet.getBoolean(6));

      }

      return user;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
