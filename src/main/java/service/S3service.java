package service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import model.S3File;

import java.util.ArrayList;
import java.util.List;

public class S3service {

  public List<S3File> listFiles(String folderName) {

    List<S3File> files = new ArrayList<>();
    String clientRegion = Region.getRegion(Regions.US_EAST_1).getName();
    String bucketName = "cloudhomework2bucket";


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
}
