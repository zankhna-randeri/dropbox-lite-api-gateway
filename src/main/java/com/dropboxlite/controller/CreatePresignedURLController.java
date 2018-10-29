package com.dropboxlite.controller;

import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.dropboxlite.config.CryptoConfig;
import com.dropboxlite.dao.FileDao;
import com.dropboxlite.exception.InvalidRequestException;
import com.dropboxlite.model.CreatePresignedUrlOutput;
import com.dropboxlite.model.FileInfo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.util.Date;

@RestController
public class CreatePresignedURLController {
  private static final Logger logger = LoggerFactory.getLogger(CreatePresignedURLController.class);

  @Autowired
  private PrivateKey privateKey;

  @Autowired
  private CryptoConfig cryptoConfig;

  @Autowired
  private FileDao fileDao;

  @GetMapping(value = "/createPresignedURL/{fileName:.+}")
  public CreatePresignedUrlOutput getPresignedUrl(@RequestHeader("userId") int userId,
                                                  @PathVariable("fileName") String fileName) throws Exception {
    logger.info("Received Create Presigned URL request");
    String decoded = null;
    try {
      decoded = URLDecoder.decode(fileName, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new InvalidRequestException("Invalid File name");
    }
    logger.info("Crypto configuration {}", cryptoConfig);
    FileInfo fileInfo = fileDao.getFileInfo(userId, decoded);



    String cloudFrontURL = String.format("%s/%d/%s",
        cryptoConfig.getCloudFrontDomain(),
        fileInfo.getUserId(), fileName);
    logger.info("Cloudfront URL: {}", cloudFrontURL);
    Date dateLessThan = DateTime.now(DateTimeZone.UTC).plusMinutes(2).toDate();
    String signedUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(cloudFrontURL,
        cryptoConfig.getCloudFrontKeyPairId(),
        privateKey,
        dateLessThan);

    return CreatePresignedUrlOutput.builder().preSignedUrl(signedUrl).build();
  }
}
