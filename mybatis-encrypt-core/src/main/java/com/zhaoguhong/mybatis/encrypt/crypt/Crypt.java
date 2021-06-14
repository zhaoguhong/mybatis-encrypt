package com.zhaoguhong.mybatis.encrypt.crypt;

/**
 * 加密接口
 *
 * @author guhong
 * @date 2021/6/12
 */
public interface Crypt {

  /**
   * 加密
   *
   * @param content 待加密的数据
   * @return 加密后的数据
   */
  String encrypt(String content);
  /**
   * 解密
   *
   * @param content 待解密的数据
   * @return 解密后的数据
   */
  String decrypt(String content);

}
