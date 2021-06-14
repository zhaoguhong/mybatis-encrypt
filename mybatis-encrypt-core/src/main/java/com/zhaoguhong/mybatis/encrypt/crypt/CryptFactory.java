package com.zhaoguhong.mybatis.encrypt.crypt;

import com.zhaoguhong.mybatis.encrypt.config.CryptConfig;
import com.zhaoguhong.mybatis.encrypt.exception.MybatisEncryptException;
import com.zhaoguhong.mybatis.encrypt.utils.StrUtils;

/**
 * 加密工厂类
 *
 * @author guhong
 * @date 2021/6/12
 */
public class CryptFactory {

  /**
   * 获取加密解密算法实例
   *
   * @return
   */
  public static Crypt getCrypt() {

    if(StrUtils.isEmpty(CryptConfig.secretKey)){
      throw new MybatisEncryptException("secretKey is required");
    }
    BaseCrypt crypt;
    if (StrUtils.isNotEmpty(CryptConfig.cryptClass)) {
      Object customCrypt = null;
      try {
        Class<?> cryptClass = Class.forName(CryptConfig.cryptClass);
        customCrypt = cryptClass.newInstance();
      } catch (ClassNotFoundException e) {
        throw new MybatisEncryptException("ClassNotFoundException : " + CryptConfig.cryptClass);
      } catch (Exception e) {
        throw new MybatisEncryptException(
            "Class: " + CryptConfig.cryptClass + " instantiation error ", e);
      }
      if (!(customCrypt instanceof BaseCrypt)) {
        throw new MybatisEncryptException(
            "Class: " + CryptConfig.cryptClass + " must be extends BaseCrypt ");
      }
      crypt = (BaseCrypt) customCrypt;
    } else {
      crypt = new AesCrypt();
    }
    crypt.setSecretKey(CryptConfig.secretKey);
    return crypt;
  }

}
