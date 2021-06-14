package com.zhaoguhong.mybatis.encrypt.utils;

/**
 * 字符串处理工具类
 *
 * @author guhong
 * @date 2021/6/12
 */
public class StrUtils {

  public static boolean isEmpty(CharSequence str) {
    return str == null || "".equals(str);
  }

  public static boolean isNotEmpty(CharSequence str) {
    return !isEmpty(str);
  }

}
