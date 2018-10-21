package service;

import model.FileInfo;

import java.sql.*;

public class FileService {

  final String jdbc_driver = "com.mysql.cj.jdbc.Driver";
  final String db_url = "jdbc:mysql://localhost:3306/dropbox_lite_api_metadata?serverTimezone=UTC";
  final String db_user = "root";
  final String db_password = "root-myDb";

  public void insertFile(FileInfo fileInfo) {
    try {
      Connection connection = DriverManager.getConnection(db_url, db_user, db_password);
      String query = "Insert into File values (?,?,?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, fileInfo.getFileName());
      statement.setInt(2, fileInfo.getUserId());
      statement.setLong(3, fileInfo.getFileCreationTimestamp());
      statement.setLong(4, fileInfo.getFileUpdateTimestamp());
      statement.setString(5, fileInfo.getS3Key());
      statement.setString(6, fileInfo.getDescription());
      statement.executeUpdate();
      int n = statement.getUpdateCount();
      if (n != 1) {
        throw new IllegalStateException("Update Entries are more than 1");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isFileExist(String s3Key) {
    try {
      Connection connection = DriverManager.getConnection(db_url, db_user, db_password);
      String query = String.format("Select * from File where s3_key='%s'", s3Key);
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();
      int n = 0;
      while(resultSet.next()) {
        n++;
      }
      connection.close();
      return n != 0;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
