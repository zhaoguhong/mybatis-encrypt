package com.zhaoguhong.mybatis.encrypt.exception;

/**
 * 加密异常类
 *
 * @author guhong
 * @date 2021/6/12
 */
public class MybatisEncryptException extends RuntimeException{

  public MybatisEncryptException() {
  }

  public MybatisEncryptException(String message, Throwable cause) {
    super(message, cause);
  }

  public MybatisEncryptException(String message) {
    super(message);
  }
}
