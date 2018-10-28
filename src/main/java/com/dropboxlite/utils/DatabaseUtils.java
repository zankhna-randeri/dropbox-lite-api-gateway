package com.dropboxlite.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {

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
