package com.zhaoguhong.mybatis.encrypt.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author guhong
 * @date 2021/6/12
 */
@Repository
public class UserJdbcDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public String getUserNameById(Long id) {
    String sql = "select user_name from user where id = ?";
    return jdbcTemplate.queryForObject(sql, new Object[]{id}, String.class);
  }

}
