package com.dropboxlite.controller;

import com.dropboxlite.dao.FileDao;
import com.dropboxlite.exception.InvalidRequestException;
import com.dropboxlite.model.FileInfo;
import com.dropboxlite.model.ListFileOutput;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ListFileControllerTest {

  @InjectMocks
  private ListFileController controller;

  @Mock
  private FileDao fileDao;

  @Test(expected = InvalidRequestException.class)
  public void invalidUserIdTest() {
    controller.listFiles(0);
  }

  @Test
  public void userWithOneFileTest() {
    //setup
    List<FileInfo> fileInfoList = new ArrayList<>();
    FileInfo fileInfo = FileInfo.builder().build();
    fileInfoList.add(fileInfo);
    Mockito.when(fileDao.listFiles(1)).thenReturn(fileInfoList);

    //call
    ListFileOutput output = controller.listFiles(1);

    //verify
    Assert.assertEquals(fileInfoList, output.getFiles());
  }

  @Test
  public void userWithNoFileTest() {
    //setup
    List<FileInfo> fileInfoList = new ArrayList<>();
    Mockito.when(fileDao.listFiles(1)).thenReturn(fileInfoList);

    //call
    ListFileOutput output = controller.listFiles(1);

    //verify
    Assert.assertEquals(fileInfoList, output.getFiles());
  }

  @Test
  public void userWithMultipleFileTest() {
    //setup
    List<FileInfo> fileInfoList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      FileInfo fileInfo = FileInfo.builder().build();
      fileInfoList.add(fileInfo);
    }
    Mockito.when(fileDao.listFiles(1)).thenReturn(fileInfoList);

    //call
    ListFileOutput output = controller.listFiles(1);

    //verify
    Assert.assertEquals(fileInfoList, output.getFiles());
  }


}
