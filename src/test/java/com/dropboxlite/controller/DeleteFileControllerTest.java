package com.dropboxlite.controller;

import com.dropboxlite.dao.FileDao;
import com.dropboxlite.exception.InvalidRequestException;
import com.dropboxlite.model.DeleteFileOutput;
import com.dropboxlite.model.FileInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class DeleteFileControllerTest {

  @InjectMocks
  private DeleteFileController controller;

  @Mock
  private FileDao fileDao;

  @Test
  public void deleteFileSuccessTest() throws Exception {
    when(fileDao.getFileInfo(1, "test.txt"))
        .thenReturn(FileInfo.builder().build());
    DeleteFileOutput expected = DeleteFileOutput.builder()
        .status("success").build();
    DeleteFileOutput output = controller.deleteFile(1, "test.txt");
    Assert.assertEquals(expected, output);
  }

  @Test(expected = InvalidRequestException.class)
  public void fileNotFoundTest() throws Exception {
    when(fileDao.getFileInfo(anyInt(), anyString()))
        .thenThrow(new FileNotFoundException("test"));
    controller.deleteFile(1, "test.txt");
  }

  @Test(expected = InvalidRequestException.class)
  public void invalidFileNameTest() {
    controller.deleteFile(1, null);
  }

  @Test(expected = InvalidRequestException.class)
  public void invalidUserIdTest() {
    controller.deleteFile(-1, "test.txt");
  }

  @Test(expected = InvalidRequestException.class)
  public void emptyFileNameTest() {
    controller.deleteFile(1, "");
  }
}


