package com.zhaoguhong.mybatis.encrypt.interceptor;

import com.zhaoguhong.mybatis.encrypt.crypt.Crypt;
import com.zhaoguhong.mybatis.encrypt.crypt.CryptFactory;
import com.zhaoguhong.mybatis.encrypt.utils.CryptFieldUtils;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

/**
 * 加密拦截器
 *
 * @author guhong
 * @date 2021/6/12
 */
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,
        Integer.class}),
})
public class EncryptInterceptor implements Interceptor {

  private Crypt crypt = CryptFactory.getCrypt();

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    encryptParameters(statementHandler.getBoundSql());
    return invocation.proceed();
  }

  private void encryptParameters(BoundSql boundSql) throws IllegalAccessException {
    // 可以考虑直接在原数据上加密处理，这种方式最简单，弊端是会改变入参的值
    Map<String, String> cryptFieldMap = CryptFieldUtils
        .getCryptMap(null, boundSql.getParameterObject());
    cryptFieldMap.forEach((key, value) -> {
      boundSql.setAdditionalParameter(key, crypt.encrypt(value));
    });
  }

  @Override
  public Object plugin(Object o) {
    return Plugin.wrap(o, this);
  }

  @Override
  public void setProperties(Properties properties) {
  }
}
