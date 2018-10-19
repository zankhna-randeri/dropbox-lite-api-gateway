package activity;

import model.ListFileOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ListFileController {

  @GetMapping("/listfile/{foldername:.+}")
  public ListFileOutput listFiles(@PathVariable String foldername) {

    List<String> files = new ArrayList<>();
    files.add("A");
    files.add("B");
    files.add("C");
    files.add(foldername);

    return ListFileOutput.builder().files(files).build();

  }

}
