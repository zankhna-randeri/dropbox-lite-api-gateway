package service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import model.S3File;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class S3service {

  String clientRegion = Region.getRegion(Regions.US_EAST_1).getName();
  String bucketName = "cloudhomework2bucket";

  public List<S3File> listFiles(String folderName) {

    List<S3File> files = new ArrayList<>();

    AmazonS3 s3Client = AmazonS3ClientBuilder
        .standard()
        .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
        .withRegion(clientRegion).build();

    ListObjectsV2Request req = new ListObjectsV2Request()
        .withBucketName(bucketName).withPrefix(folderName + "/").withDelimiter("/");


    ListObjectsV2Result result = s3Client.listObjectsV2(req);
    for (S3ObjectSummary summary : result.getObjectSummaries()) {
      System.out.println(String.format(" - %s (size: %d) LastModified : %s",
          summary.getKey(),
          summary.getSize(),
          summary.getLastModified()));
      S3File file = new S3File();
      file.setKey(summary.getKey());
      file.setCreationTime(summary.getLastModified().getTime());
      file.setLastModified(summary.getLastModified().getTime());
      file.setSize(String.valueOf(summary.getSize()));
      files.add(file);
    }
    return files;

  }

  public boolean createFolder(String folderName) {
    boolean success = true;
    return success;
  }

  public boolean uploadFile(InputStream streamFile, String folderName) {

    boolean result = true;
    String keyName = folderName + "/";

    try {
      AmazonS3 s3Client = AmazonS3ClientBuilder
          .standard()
          .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
          .withRegion(clientRegion).build();

      byte[] contentByte = IOUtils.toByteArray(streamFile);
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(contentByte.length);
      PutObjectRequest putRequest = new PutObjectRequest(bucketName, keyName, streamFile, metadata);
      s3Client.putObject(putRequest);

    } catch (IOException e) {
      result = false;
      e.printStackTrace();
    } catch (Exception e) {
      result = false;
      e.printStackTrace();
    }
    return result;
  }
}
