package com.dropboxlite.utils;

import java.sql.*;

public class DatabaseUtils {
  private static final String DB_URL = "jdbc:mysql://localhost:3306/dropbox_lite_api_metadata?serverTimezone=UTC";
  private static final String DB_USER = "root";
  private static final String DB_PASSWORD = "root-myDb";

  public static Connection getDatabaseConnection() throws SQLException {
    return DriverManager.getConnection(DatabaseUtils.DB_URL,
        DatabaseUtils.DB_USER,
        DatabaseUtils.DB_PASSWORD);
  }

  public static boolean isRecordExist(Connection connection, String query) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      ResultSet resultSet = statement.executeQuery();
      int n = 0;
      while (resultSet.next()) {
        n++;
      }
      return n != 0;
    }
  }
}
