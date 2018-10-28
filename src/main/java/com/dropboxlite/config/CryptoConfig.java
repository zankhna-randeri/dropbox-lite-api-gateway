package com.dropboxlite.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("crypto")
@Getter
@Setter
@Component
@ToString
public class CryptoConfig {
  private String cloudFrontDomain;
  private String cloudFrontKeyPairId;
  private String secretkey;
}
