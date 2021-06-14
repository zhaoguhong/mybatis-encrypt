package com.zhaoguhong.mybatis.encrypt.utils;

import com.zhaoguhong.mybatis.encrypt.annotation.CryptField;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加密字段处理工具类
 *
 * @author guhong
 * @date 2021/6/12
 */
public class CryptFieldUtils {

  private static final ConcurrentHashMap<String, Set<Field>> CRYPT_FIELDS = new ConcurrentHashMap<>();


  public static Map<String, String> getCryptMap(String prefix, Object params)
      throws IllegalAccessException {
    if (params == null || params instanceof Collections) {
      return Collections.emptyMap();
    }
    if (prefix == null) {
      prefix = "";
    }
    if (prefix != "") {
      prefix = prefix + ".";
    }
    Map<String, String> cryptFieldMap = new HashMap<>();
    if (params instanceof Map) {
      Map<String, Object> paramsMap = (Map<String, Object>) params;
      for (Entry<String, Object> entry : paramsMap.entrySet()) {
        cryptFieldMap.putAll(getCryptMap(prefix + entry.getKey(), entry.getValue()));
      }
      return cryptFieldMap;
    }
    Set<Field> cryptFields = getCryptFields(params);
    for (Field field : cryptFields) {
      Object data = field.get(params);
      if (data != null && !"".equals(data)) {
        cryptFieldMap.put(prefix + field.getName(), data.toString());
      }
    }
    return cryptFieldMap;
  }

  public static Set<Field> getCryptFields(Object params) {
    if (params == null || params instanceof Map || params instanceof Collection) {
      return Collections.emptySet();
    }
    String className = params.getClass().getName();
    if (!CRYPT_FIELDS.contains(className)) {
      Field fields[] = params.getClass().getDeclaredFields();
      for (Field field : fields) {
        CryptField cryptFieldAnnotation = field.getAnnotation(CryptField.class);
        if (cryptFieldAnnotation != null) {
          Set<Field> strings = CRYPT_FIELDS
              .computeIfAbsent(className, key -> new HashSet<>());
          field.setAccessible(true);
          strings.add(field);
        }
      }
      CRYPT_FIELDS.putIfAbsent(className, Collections.emptySet());
    }
    return CRYPT_FIELDS.get(className);
  }
}
