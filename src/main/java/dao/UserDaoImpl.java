package dao;

import model.User;
import utils.DatabaseUtils;

import java.sql.*;

public class UserDaoImpl implements UserDao {

  private static final String USER_EXIST_QUERY_FORMAT =
      "Select * from User where user_email='%s'";

  private static final String USER_INSERT_QUERY_FORMAT =
      "Insert into User (first_name,last_name,user_email,password)" + " " +
      "values (?,?,?,?)";

  @Override
  public boolean isUserExist(String userEmail) {
    try {
      String query = String.format(USER_EXIST_QUERY_FORMAT, userEmail);
      return DatabaseUtils.isRecordExist(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int registerUser(User user) {
    try {
      Connection connection = DatabaseUtils.getDatabaseConnection();

      PreparedStatement statement = connection.prepareStatement(USER_INSERT_QUERY_FORMAT, Statement.RETURN_GENERATED_KEYS);
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
}
