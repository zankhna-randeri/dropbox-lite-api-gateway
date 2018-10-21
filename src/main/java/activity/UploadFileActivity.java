package activity;

import model.FileInfo;
import model.UploadFileOutput;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.FileService;
import service.S3service;

import java.io.IOException;

@RestController
public class UploadFileActivity {

  //  @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")

  @PostMapping(value = "/upload", consumes = "multipart/form-data")
  public UploadFileOutput upload(@RequestHeader(value = "userid") int userid,
                                 @RequestParam("file") MultipartFile input,
                                 @RequestParam("description") String description) {
    // 1. get file stream
    // 2. validate all values
    // 3. upload to s3
    // 4. update database entry
    // 5. return success else failure

    //TODO : OriginalFileName() doesn not work on Opera.
    String fileName = input.getOriginalFilename();
    String s3Key = userid + "/" + fileName;
    long uploadTimeStamp = System.currentTimeMillis();
    long updateTimeStamp = System.currentTimeMillis();
    FileInfo fileInfo = FileInfo.builder()
        .userId(userid)
        .fileName(fileName)
        .s3Key(s3Key)
        .fileCreationTimestamp(uploadTimeStamp)
        .fileUpdateTimestamp(updateTimeStamp)
        .description(description)
        .build();
    FileService fileService = new FileService();
    S3service s3Service = new S3service();
    if (fileService.isFileExist(s3Key)) {
      //TODO: Update File

    } else {
      try {

        if (s3Service.uploadFile(input.getInputStream(), input.getSize(), Integer.toString(userid),fileName)) {
          fileService.insertFile(fileInfo);
          return UploadFileOutput.builder()
              .result(true)
              .build();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    System.out.println(description);
    return UploadFileOutput.builder()
        .result(false)
        .build();
  }

}
