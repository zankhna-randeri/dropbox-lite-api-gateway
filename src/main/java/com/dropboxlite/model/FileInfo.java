package com.dropboxlite.model;

import lombok.Builder;
import lombok.Data;

/**
 * FileInfo contains
 * - fileId : unique id of file
 * - fileName : name of file
 * - userId : id of file owner
 * - fileCreationTimestamp : file creation timestamp
 * - fileUpdateTimestamp : file last modification timestamp
 * - s3Key : s3Key representing file location in s3 bucket.
 */

@Data
@Builder
public class FileInfo {
  private String fileName;
  private int userId;
  private long fileCreationTimestamp;
  private long fileUpdateTimestamp;
  private String s3Key;
  private String description;
  private long fileSize;
  private String userName;
}
