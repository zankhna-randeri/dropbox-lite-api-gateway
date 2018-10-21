package service;

import model.User;

import java.sql.*;

public class UserService {

  final String jdbc_driver = "com.mysql.cj.jdbc.Driver";
  final String db_url = "jdbc:mysql://localhost:3306/dropbox_lite_api_metadata?serverTimezone=UTC";
  final String db_user = "root";
  final String db_password = "root-myDb";

  public boolean isUserExist(String userEmail) {
    try {
      Connection connection = DriverManager.getConnection(db_url, db_user, db_password);
      Statement statement = connection.createStatement();
      String query = String.format("Select * from User where user_email='%s'", userEmail);
      ResultSet rs = statement.executeQuery(query);
      connection.close();
      return (rs == null);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void insertUser(User user) {
    try {
      Connection connection = DriverManager.getConnection(db_url, db_user, db_password);
      String query = "Insert into User (first_name,last_name,user_email,password) values (?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, user.getFirstName());
      statement.setString(2, user.getLastName());
      statement.setString(3, user.getUserEmail());
      statement.setString(4, user.getPassword());
      System.out.println("Result of insert:" + statement.execute());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }
}
