package activity;

import model.UploadFileOutput;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.S3service;

import java.io.IOException;

@RestController
public class UploadFileActivity {

  //  @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")

  @PostMapping(value = "/upload", consumes = "multipart/form-data")
  public UploadFileOutput upload(@RequestParam("file") MultipartFile input,
                                 @RequestParam("other-args") String otherargs) {
    // 1. get file stream
    // 2. validate all values
    // 3. upload to s3
    // 4. update database entry
    // 5. return success else failure

    boolean result = false;
    S3service uploadFile = new S3service();
    try {
      result = uploadFile.uploadFile(input.getInputStream(), otherargs);

    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println(otherargs);

    return UploadFileOutput.builder()
        .result(result)
        .build();
  }

}
