package activity;

import model.UploadFileOutput;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
public class UploadFileActivity {

  @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")
  public UploadFileOutput upload(@RequestParam("file") MultipartFile input,
                                 @RequestParam("other-args") String otherargs) {
    // 1. get file stream
    // 2. validate all values
    // 3. upload to s3
    // 4. update database entry
    // 5. return success else failure

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(input.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(otherargs);

    return UploadFileOutput.builder()
        .result("success")
        .build();
  }

}
