package com.zhaoguhong.mybatis.encrypt.handler;

import com.zhaoguhong.mybatis.encrypt.crypt.Crypt;
import com.zhaoguhong.mybatis.encrypt.crypt.CryptFactory;
import com.zhaoguhong.mybatis.encrypt.utils.StrUtils;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 自定义的加密类型处理器
 *
 * @author guhong
 * @date 2021/6/12
 */
public class CryptTypeHandler extends BaseTypeHandler<String> {

  private Crypt crypt = CryptFactory.getCrypt();;

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
      throws SQLException {
    if (!"".equals(parameter)) {
      parameter = crypt.encrypt(parameter);
    }
    ps.setString(i, parameter);
  }

  @Override
  public String getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    String columnValue = rs.getString(columnName);
    if (StrUtils.isNotEmpty(columnValue)) {
      columnValue = crypt.decrypt(columnValue);
    }
    return columnValue;
  }

  @Override
  public String getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
    String columnValue = rs.getString(columnIndex);
    if (StrUtils.isNotEmpty(columnValue)) {
      columnValue = crypt.decrypt(columnValue);
    }
    return columnValue;
  }

  @Override
  public String getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    String columnValue = cs.getString(columnIndex);
    if (StrUtils.isNotEmpty(columnValue)) {
      columnValue = crypt.decrypt(columnValue);
    }
    return columnValue;
  }

}
