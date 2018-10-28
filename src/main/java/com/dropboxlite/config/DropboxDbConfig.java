package com.dropboxlite.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@ConfigurationProperties("dropbox")
public class DropboxDbConfig {
  private String connectionString;
  private String username;
  private String password;
}
