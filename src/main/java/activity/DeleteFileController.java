package activity;

import dao.FileDao;
import exception.InvalidRequestException;
import model.DeleteFileOutput;
import model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class DeleteFileController {

  @Autowired
  private FileDao fileDao;

  @DeleteMapping("/delete/{userId:[0-9]+}/{fileName:.+}")
  public DeleteFileOutput deleteFile(@PathVariable int userId, @PathVariable String fileName) {
    FileInfo fileInfo = null;
    try {
      fileInfo = fileDao.getFileInfo(userId, fileName);
      fileDao.deleteFileFromS3(fileInfo.getS3Key());
      fileDao.deleteFileFromDB(userId, fileName);
    } catch (FileNotFoundException e) {
      throw new InvalidRequestException("File not found");
    }
    return DeleteFileOutput.builder().status("success").build();
  }

}
