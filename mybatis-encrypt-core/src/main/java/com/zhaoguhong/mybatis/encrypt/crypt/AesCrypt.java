package com.zhaoguhong.mybatis.encrypt.crypt;

import com.zhaoguhong.mybatis.encrypt.exception.MybatisEncryptException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * mybatis AES加密
 *
 * @author guhong
 * @date 2021/6/12
 */
public class AesCrypt extends BaseCrypt {

  private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

  @Override
  public String encrypt(String content) {
    String secretKey = getSecretKey();
    try {
      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
          "AES");
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      byte[] bytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(bytes);
    } catch (Exception e) {
      throw new MybatisEncryptException("aes decrypt error", e);
    }
  }

  @Override
  public String decrypt(String content) {
    String secretKey = getSecretKey();
    try {
      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
          "AES");
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
      byte[] decode = Base64.getDecoder().decode(content);
      byte[] bytes = cipher.doFinal(decode);
      return new String(bytes, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new MybatisEncryptException("aes decrypt error", e);
    }
  }

  @Override
  boolean checkSecretKey() {
    return getSecretKey().length() == 16;
  }
}
