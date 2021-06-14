package com.zhaoguhong.mybatis.encrypt.test.util;

import com.zhaoguhong.mybatis.encrypt.test.entity.User;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author guhong
 * @date 2021/6/12
 */
public class UserGenerator {

  public static User generate() {
    return User.builder()
        .userName(UUID.randomUUID().toString())
        .password(UUID.randomUUID().toString())
        .age(20)
        .build();
  }

  public static Map<String, Object>  generateMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("userName", UUID.randomUUID().toString());
    map.put("password", UUID.randomUUID().toString());
    map.put("age", 20);
    return map;
  }

}
