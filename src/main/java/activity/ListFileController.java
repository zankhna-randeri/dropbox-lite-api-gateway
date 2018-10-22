package activity;

import dao.FileDao;
import model.ListFileOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListFileController {

  @Autowired
  private FileDao fileDao;

  @GetMapping("/listfile")
  public ListFileOutput listFiles(@RequestHeader(value = "userid") int userId) {
    return ListFileOutput.builder().files(fileDao.listFiles(userId)).build();
  }

}
