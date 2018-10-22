package com.dropboxlite.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dropboxlite.model.FileInfo;
import com.dropboxlite.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FileDaoImpl implements FileDao, AutoCloseable {

  private static final String FILE_EXIST_QUERY_FORMAT =
      "Select * from File where user_id=%d and file_name = '%s'";

  private static final String FILE_INSERT_QUERY_FORMAT =
      "Insert into File " +
          "(file_name, " +
          " user_id, " +
          " upload_timestamp, " +
          " update_timestamp," +
          " s3_key, " +
          " file_description, " +
          " file_size)" +
          " values (?,?,?,?,?,?,?) " +
          "ON DUPLICATE KEY " +
          "UPDATE " +
          "update_timestamp=?," +
          "file_description=?," +
          "file_size=?";

  private static final String LIST_FILE_QUERY_FORMAT =
      "Select * from File where user_id=%d";

  private static final String DELETE_FILE_QUERY_FORMAT =
      "Delete from File where user_id=%d and file_name = '%s'";


  private static final String BUCKET_NAME = "cloudhomework2bucket";

  private final Connection connection;

  @Autowired
  private AmazonS3 s3Client;

  public FileDaoImpl() throws SQLException {
    this.connection = DatabaseUtils.getDatabaseConnection();
  }

  @Override
  public boolean isFileExist(int userId, String fileName) {
    try {
      String query = String.format(FILE_EXIST_QUERY_FORMAT, userId, fileName);
      return DatabaseUtils.isRecordExist(connection, query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void insertFile(FileInfo fileInfo) {
    try (PreparedStatement statement = connection.prepareStatement(FILE_INSERT_QUERY_FORMAT,
        Statement.RETURN_GENERATED_KEYS)) {

      //Used on file insert
      statement.setString(1, fileInfo.getFileName());
      statement.setInt(2, fileInfo.getUserId());
      statement.setLong(3, fileInfo.getFileCreationTimestamp());
      statement.setLong(4, fileInfo.getFileUpdateTimestamp());
      statement.setString(5, fileInfo.getS3Key());
      statement.setString(6, fileInfo.getDescription());
      statement.setLong(7, fileInfo.getFileSize());

      //Used on file Update
      statement.setLong(8, fileInfo.getFileUpdateTimestamp());
      statement.setString(9, fileInfo.getDescription());
      statement.setLong(10, fileInfo.getFileSize());
      statement.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean uploadFile(InputStream streamFile, FileInfo fileInfo) {
    String keyName = fileInfo.getUserId() + "/" + fileInfo.getFileName();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(fileInfo.getFileSize());
    PutObjectRequest putRequest = new PutObjectRequest(BUCKET_NAME, keyName, streamFile, metadata);
    s3Client.putObject(putRequest);
    return true;
  }

  @Override
  public List<FileInfo> listFiles(int userId) {
    String query = String.format(LIST_FILE_QUERY_FORMAT, userId);
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      ResultSet resultSet = statement.executeQuery();
      List<FileInfo> fileInfoList = new ArrayList<>();
      while (resultSet.next()) {
        FileInfo fileInfo = FileInfo.builder()
            .fileName(resultSet.getString(1))
            .userId(resultSet.getInt(2))
            .fileCreationTimestamp(resultSet.getLong(3))
            .fileUpdateTimestamp(resultSet.getLong(4))
            .s3Key(resultSet.getString(5))
            .description(resultSet.getString(6))
            .fileSize(resultSet.getLong(7)).build();
        fileInfoList.add(fileInfo);
      }
      return fileInfoList;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean createFolder(String folderName) {
    try {
      folderName = folderName + "/";
      InputStream folderStream = new InputStream() {
        @Override
        public int read() throws IOException {
          return -1;
        }
      };
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(0L);
      PutObjectRequest putRequest = new PutObjectRequest(BUCKET_NAME, folderName, folderStream, metadata);
      s3Client.putObject(putRequest);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public FileInfo getFileInfo(int userId, String fileName) throws FileNotFoundException {
    String query = String.format(FILE_EXIST_QUERY_FORMAT, userId, fileName);
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      ResultSet resultSet = statement.executeQuery();
      FileInfo fileInfo = null;
      if (resultSet.next()) {
        fileInfo = FileInfo.builder()
            .fileName(resultSet.getString(1))
            .userId(resultSet.getInt(2))
            .fileUpdateTimestamp(resultSet.getLong(3))
            .fileUpdateTimestamp(resultSet.getLong(4))
            .s3Key(resultSet.getString(5))
            .description(resultSet.getString(6))
            .fileSize(resultSet.getLong(7)).build();
      }
      return fileInfo;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteFileFromS3(String s3Key) {
    s3Client.deleteObject(BUCKET_NAME, s3Key);
  }

  @Override
  public void deleteFileFromDB(int userId, String fileName) throws FileNotFoundException {
    String query = String.format(DELETE_FILE_QUERY_FORMAT, userId, fileName);
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() throws Exception {
    connection.close();
  }
}
