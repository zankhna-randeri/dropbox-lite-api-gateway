package com.dropboxlite.controller;

import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.dropboxlite.dao.FileDao;
import com.dropboxlite.exception.InvalidRequestException;
import com.dropboxlite.model.CreatePresignedUrlOutput;
import com.dropboxlite.model.FileInfo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

@RestController
public class CreatePresignedURLController {

  private static final String CLOUDFRONT_DOMAIN = "https://d3i8d3bd718mbp.cloudfront.net";
  private static final String CLOUDFRONT_KEYPAIR_ID = "APKAJRUZVZY4QRPKTLVQ";

  @Autowired
  FileDao fileDao;

  public static PrivateKey loadPrivateKey(String fileName)
      throws IOException, GeneralSecurityException {
    try (
        InputStream is = fileName.getClass().getResourceAsStream("/" + fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      StringBuilder builder = new StringBuilder();
      boolean inKey = false;
      for (String line = br.readLine(); line != null; line = br.readLine()) {
        if (!inKey) {
          if (line.startsWith("-----BEGIN ") &&
              line.endsWith(" PRIVATE KEY-----")) {
            inKey = true;
          }
          continue;
        } else {
          if (line.startsWith("-----END ") &&
              line.endsWith(" PRIVATE KEY-----")) {
            inKey = false;
            break;
          }
          builder.append(line);
        }
      }

      byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
      KeyFactory kf = KeyFactory.getInstance("RSA");
      return kf.generatePrivate(keySpec);
    }
  }

  @GetMapping(value = "/createPresignedURL/{fileName:.+}")
  public CreatePresignedUrlOutput getPresignedUrl(@RequestHeader("userId") int userId,
                                                  @PathVariable("fileName") String fileName) throws Exception {

    try {
      fileName = URLDecoder.decode(fileName, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new InvalidRequestException("Invalid File name");
    }
    RSAPrivateKey privateKey = getRsaPrivateKey();

    FileInfo fileInfo = fileDao.getFileInfo(userId, fileName);

    String cloudFrontURL = String.format("%s/%s", CLOUDFRONT_DOMAIN, fileInfo.getS3Key());
    Date dateLessThan = DateTime.now(DateTimeZone.UTC).plusMinutes(2).toDate();
    String signedUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(cloudFrontURL,
        CLOUDFRONT_KEYPAIR_ID,
        privateKey,
        dateLessThan);

    return CreatePresignedUrlOutput.builder().preSignedUrl(signedUrl).build();
  }

  private RSAPrivateKey getRsaPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    //TODO: add a logic to read file from s3 & decrypt and cache in memory
    String derFilePath = "/Users/jalpan/Desktop/cloudfront-signed-url/dropbox-lite-private.der";
    byte[] derPrivateKeyBytes = Files.readAllBytes(Paths.get(derFilePath));
    PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(derPrivateKeyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return (RSAPrivateKey) keyFactory.generatePrivate(privSpec);
  }

}
