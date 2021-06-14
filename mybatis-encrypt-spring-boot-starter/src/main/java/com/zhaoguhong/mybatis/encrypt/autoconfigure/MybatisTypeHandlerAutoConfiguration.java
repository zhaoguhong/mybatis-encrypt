package com.zhaoguhong.mybatis.encrypt.autoconfigure;

import com.zhaoguhong.mybatis.encrypt.config.CryptConfig;
import com.zhaoguhong.mybatis.encrypt.handler.CryptTypeHandler;
import javax.annotation.PostConstruct;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * TypeHandler 自动配置类，要在 mybatis 的自动配置之前执行
 *
 * @author guhong
 * @date 2021/6/12
 */
@Configuration
@EnableConfigurationProperties(MybatisEncryptProperties.class)
@AutoConfigureBefore(MybatisAutoConfiguration.class)
public class MybatisTypeHandlerAutoConfiguration {

  @Autowired
  private MybatisProperties mybatisProperties;

  @Autowired
  private MybatisEncryptProperties properties;

  @PostConstruct
  public void init() {
    CryptConfig.secretKey = properties.getSecret();
    CryptConfig.cryptClass = properties.getCryptClass();
    String cryptTypeHandlerPackageName = CryptTypeHandler.class.getPackage().getName();
    if (StringUtils.hasText(mybatisProperties.getTypeAliasesPackage())) {
      mybatisProperties.setTypeAliasesPackage(
          mybatisProperties.getTypeAliasesPackage() + "," + cryptTypeHandlerPackageName);
    } else {
      mybatisProperties.setTypeAliasesPackage(cryptTypeHandlerPackageName);
    }
  }

}
