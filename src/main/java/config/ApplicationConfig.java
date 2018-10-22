package config;

import dao.FileDao;
import dao.FileDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  public FileDao getFileDao() {
    return new FileDaoImpl();
  }

  @Bean
  public UserDao getUserDao() {
    return new UserDaoImpl();
  }
}
