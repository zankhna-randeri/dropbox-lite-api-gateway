package com.dropboxlite.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Crypto {

  private static final Logger logger = LoggerFactory.getLogger(Crypto.class);

  @Autowired
  private ResourceLoader resourceLoader;

  public RSAPrivateKey getRSAKey(String secretKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
      IllegalBlockSizeException,
      InvalidKeyException, BadPaddingException, NoSuchPaddingException, IOException {
    byte[] key = decrypt(secretKey);
    PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(key);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return (RSAPrivateKey) keyFactory.generatePrivate(privSpec);
  }

  private byte[] decrypt(String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException,
      InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    byte[] key = secretKey.getBytes(); //"anyone frm cloudfront in dropbox".getBytes();
    logger.info("Key size: {}", key.length);
    SecretKey secKey = new SecretKeySpec(key, "AES");
    Cipher aesCipher = Cipher.getInstance("AES");
    Resource res = resourceLoader.getResource("classpath:encryptedtext.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream()));
    String line;
    StringBuilder builder = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      builder.append(line);
    }
    byte[] cipherText = Base64.getDecoder().decode(builder.toString().getBytes());
    logger.info("cipherText size {}", cipherText.length);
    aesCipher.init(Cipher.DECRYPT_MODE, secKey);
    return aesCipher.doFinal(cipherText);
  }
}
