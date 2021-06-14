package com.zhaoguhong.mybatis.encrypt.crypt;

import com.zhaoguhong.mybatis.encrypt.exception.MybatisEncryptException;

/**
 * 加密基类
 *
 * @author guhong
 * @date 2021/6/12
 */
public abstract class BaseCrypt implements Crypt {

  private String secretKey = "IKhEA1Oyt7sr1HeH";

  /**
   * 设置密钥
   *
   * @param secretKey
   */
  protected void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
    if (!checkSecretKey()) {
      throw new MybatisEncryptException("SecretKey format error:");
    }
  }

  /**
   * 获取密钥
   */
  protected String getSecretKey() {
    return secretKey;
  }

  /**
   * 校验密钥格式是否正确
   *
   * @return
   */
  abstract boolean checkSecretKey();

}
