package com.dropboxlite;

import com.dropboxlite.dao.FileDao;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class ApplicationTest {

  @Bean
  public FileDao fileDao() {
    return Mockito.mock(FileDao.class);
  }

}