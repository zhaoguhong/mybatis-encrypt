package com.zhaoguhong.mybatis.encrypt.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 加密配置类
 *
 * @author guhong
 * @date 2021/6/12
 */
@ConfigurationProperties(prefix = "mybatis.encrypt")
public class MybatisEncryptProperties {

  /**
   * 密钥
   */
  private String secret;

  /**
   * 自定义加密解密class
   */
  private String cryptClass;


  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getCryptClass() {
    return cryptClass;
  }

  public void setCryptClass(String cryptClass) {
    this.cryptClass = cryptClass;
  }

}
