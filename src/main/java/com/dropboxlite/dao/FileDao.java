package com.dropboxlite.dao;

import com.dropboxlite.model.FileInfo;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface FileDao {
  /**
   * determines file exist in database or not
   *
   * @param userId   id of user
   * @param fileName name of file
   * @return true if file present in db else false
   */
  boolean isFileExist(int userId, String fileName);


  /**
   * insert file metadata record into database
   *
   * @param fileInfo file metadata
   */
  void insertFile(FileInfo fileInfo);

  /**
   * Delete file from database
   *
   * @param userId   id of user
   * @param fileName name of file
   * @throws FileNotFoundException when file does not exist on database
   */
  void deleteFileFromDB(int userId, String fileName) throws FileNotFoundException;


  /**
   * Upload file to s3 bucket
   *
   * @param streamFile file stream to be uploaded
   * @param fileInfo   metadata of file to be uploaded
   * @return true if file successfully uploaded to s3 else false
   */
  boolean uploadFile(InputStream streamFile, FileInfo fileInfo);

  /**
   * List all files of given userId
   *
   * @param userId id of user
   * @return list of files from database
   */
  List<FileInfo> listFiles(int userId);

  /**
   * @param folderName name of prefix for s3 bucket
   * @return true if folder created successfully else false
   */
  boolean createFolder(String folderName);

  /**
   * Returns file metadata of given userId and fileName from database
   *
   * @param userId   id of user
   * @param fileName name of file
   * @return
   */
  FileInfo getFileInfo(int userId, String fileName) throws FileNotFoundException;

  /**
   * Deletes object from s3 bucket with given key
   *
   * @param s3Key
   */
  void deleteFileFromS3(String s3Key);
}
