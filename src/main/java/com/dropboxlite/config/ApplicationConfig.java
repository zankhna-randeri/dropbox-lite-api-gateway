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
import com.dropboxlite.utils.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class ApplicationConfig {
  private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
  private static final String REGION = Region.getRegion(Regions.US_EAST_1).getName();

  @Autowired
  private CryptoConfig cryptoConfig;

  @Bean
  public Connection connection(DropboxDbConfig dbConfig) throws SQLException {
    logger.info("Connection configuration: {}", dbConfig);
    return DriverManager.getConnection(dbConfig.getConnectionString(), dbConfig.getUsername(),
        dbConfig.getPassword());
  }

  @Bean(destroyMethod = "close")
  public FileDao getFileDao(Connection connection) throws SQLException {
    return new FileDaoImpl(connection);
  }

  @Bean(destroyMethod = "close")
  public UserDao getUserDao(Connection connection) throws SQLException {
    return new UserDaoImpl(connection);
  }

  @Bean
  public AmazonS3 getS3Client() {
    return AmazonS3ClientBuilder
        .standard()
        .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
        .withRegion(REGION).build();
  }

  @Bean
  public PrivateKey privateKey(Crypto crypto) throws Exception {
    logger.info("Crypto configuration: {}", cryptoConfig);
    return crypto.getRSAKey(cryptoConfig.getSecretkey());
  }

  @Bean
  public Crypto crypto() {
    return new Crypto();
  }
}
