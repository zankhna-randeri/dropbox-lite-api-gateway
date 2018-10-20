package activity;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import model.DeleteFileOutput;
import model.FileInfo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteFileController {

  @DeleteMapping("/delete/{userId:[0-9]+}/{fileName:.+}")
  public DeleteFileOutput deleteFile(@PathVariable int userId, @PathVariable String fileName) {
    String fileId = userId + fileName;
    FileInfo fileInfo = getFileInfo(fileId);
    deleteFromS3(fileInfo.getS3Key());
    deleteFromDB(fileId);
    return DeleteFileOutput.builder().status("success").build();
  }

  private FileInfo getFileInfo(String fileId) {
    // FIXME : implement MySQL
    return FileInfo.builder()
        .fileId(fileId)
        .fileCreationTimestamp(374734783)
        .fileName("img1.jpg")
        .fileUpdateTimestamp(615255522)
        .s3Key("zranderi/Undeploy.yml")
        .userId(1)
        .build();
  }

  private void deleteFromS3(String s3Key) {
    String bucketName = "cloudhomework2bucket";
    String clientRegion = Region.getRegion(Regions.US_EAST_1).getName();

    AmazonS3 s3Client = AmazonS3ClientBuilder
        .standard()
        .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
        .withRegion(clientRegion).build();
    s3Client.deleteObject(bucketName, s3Key);
  }

  private void deleteFromDB(String fileId) {
    // FIXME : implement MySQL

  }
}
