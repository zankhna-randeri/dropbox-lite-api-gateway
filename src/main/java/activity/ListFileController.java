package activity;

import IO.ListFileOutput;
import model.S3File;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import service.S3service;

import java.util.List;

@RestController
public class ListFileController {

  @GetMapping("/listfile/{foldername:.+}")
  public ListFileOutput listFiles(@PathVariable String foldername) {
    S3service  s3List = new S3service();
    List<S3File> files = s3List.listFiles(foldername);
    return ListFileOutput.builder().files(files).build();

  }

}
