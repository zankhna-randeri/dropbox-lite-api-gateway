package com.dropboxlite.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.dropboxlite.dao.FileDao;
import com.dropboxlite.dao.FileDaoImpl;
import com.dropboxlite.dao.UserDao;
import com.dropboxlite.dao.UserDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class ApplicationConfig {

  private static final String REGION = Region.getRegion(Regions.US_EAST_1).getName();

  @Bean(destroyMethod = "close")
  public FileDao getFileDao() throws SQLException {
    return new FileDaoImpl();
  }

  @Bean(destroyMethod = "close")
  public UserDao getUserDao() throws SQLException {
    return new UserDaoImpl();
  }

  @Bean
  public AmazonS3 getS3Client() {
    return AmazonS3ClientBuilder
        .standard()
        .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
        .withRegion(REGION).build();
  }
}
