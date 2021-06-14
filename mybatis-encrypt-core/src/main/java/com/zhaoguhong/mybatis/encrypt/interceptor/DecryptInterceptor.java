package com.zhaoguhong.mybatis.encrypt.interceptor;

import com.zhaoguhong.mybatis.encrypt.crypt.Crypt;
import com.zhaoguhong.mybatis.encrypt.crypt.CryptFactory;
import com.zhaoguhong.mybatis.encrypt.utils.CryptFieldUtils;
import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

/**
 * 解密拦截器
 *
 * @author guhong
 * @date 2021/6/12
 */
@Intercepts({
    @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {
        Statement.class}),
})
public class DecryptInterceptor implements Interceptor {

  private Crypt crypt = CryptFactory.getCrypt();

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    List<Object> result = (List<Object>) invocation.proceed();
    if (result != null && !result.isEmpty()) {
      for (Object obj : result) {
        Set<Field> cryptFields = CryptFieldUtils.getCryptFields(obj);
        for (Field cryptField : cryptFields) {
          Object ciphertext = cryptField.get(obj);
          if (ciphertext instanceof String && !"".equals(ciphertext)) {
            cryptField.set(obj, crypt.decrypt(ciphertext.toString()));
          }
        }
      }
    }
    return result;
  }

  @Override
  public Object plugin(Object o) {
    return Plugin.wrap(o, this);
  }

  @Override
  public void setProperties(Properties properties) {
  }
}
