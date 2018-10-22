package com.dropboxlite.controller;

import com.dropboxlite.dao.FileDao;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UploadFileControllerTest {

  @InjectMocks
  private UploadFileController controller;

  @Mock
  private FileDao fileDao;


}
