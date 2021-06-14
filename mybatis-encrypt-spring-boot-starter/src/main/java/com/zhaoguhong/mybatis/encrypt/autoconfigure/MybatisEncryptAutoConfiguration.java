package com.zhaoguhong.mybatis.encrypt.autoconfigure;

import com.zhaoguhong.mybatis.encrypt.interceptor.DecryptInterceptor;
import com.zhaoguhong.mybatis.encrypt.interceptor.EncryptInterceptor;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 加密自动配置类，要在 mybatis 的自动配置后执行
 *
 * @author guhong
 * @date 2021/6/12
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@EnableConfigurationProperties(MybatisEncryptProperties.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MybatisEncryptAutoConfiguration {

  @Autowired
  private MybatisEncryptProperties properties;

  @Autowired
  private List<SqlSessionFactory> sqlSessionFactoryList;

  @PostConstruct
  public void addPageInterceptor() {
    EncryptInterceptor sensitiveAndEncryptWriteInterceptor = new EncryptInterceptor();
    DecryptInterceptor decryptInterceptor = new DecryptInterceptor();
    for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
      sqlSessionFactory.getConfiguration().addInterceptor(sensitiveAndEncryptWriteInterceptor);
      sqlSessionFactory.getConfiguration().addInterceptor(decryptInterceptor);
    }
  }

}
