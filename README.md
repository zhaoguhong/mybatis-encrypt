# mybatis-encrypt

一个简单的 mybatis 加密组件

只需要简单的配置，就可以在进行数据入库自动加密，出库自动解密

## 配置
### 引入依赖
```xml
<dependency>
  <groupId>com.zhaoguhong</groupId>
  <artifactId>mybatis-encrypt-spring-boot-starter</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

### 配置密钥
直接在 spring boot 配置文件里面配置密钥
```
# 配置mybatis插件加密密钥
mybatis.encrypt.secret=wliFCmMMTw7kBWkF
```

## 使用

###  注解 CryptField
简单的说，只要你的 java bean 属性上加了这个注解

**以这个 java bean 作为入参，会对该参数的数据自动加密**

**以这个 java bean 作为入出参（也可以是 List 中包含），会对该参数的数据自动加密**

#### 举个栗子

定义一个java bean, 在 userName 上添加该注解

```java
public class User implements Serializable {

  private Long id;
  private String password;
  @CryptField
  private String userName;
  private Integer age;
 // 省略 getter setter
  
}
```
添加一个 insert 方法
```java
  @Insert({
      "insert into user (`id`, `password`, ",
      "`user_name`, `age`)",
      "values (#{id,jdbcType=BIGINT}, #{password,jdbcType=VARCHAR}, ",
      "#{userName,jdbcType=VARCHAR}, #{age,jdbcType=INTEGER})"
  })
  @Options(useGeneratedKeys = true)
  int insert(User record);
```
那么保存到数据库的数据中，userName 就是加密过的数据了

我们再来看一下查询
```java
  @Select({
    "select",
    "`id`, `password`, `user_name`, `age`",
    "from user",
    "where `id` = #{id,jdbcType=BIGINT}"
})
@Results({
    @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
    @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
    @Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR),
    @Result(column="age", property="age", jdbcType=JdbcType.INTEGER)
})
  User selectByPrimaryKey(Long id);
```
返回的 user 对象中，userName 字段已经被自动解密

###  CryptTypeHandler

注解的方式虽然方便，但有时候不够灵活，如果我们传入的参数不是 java bean 对象，这时候自动加解密就不太好用了

cryptTypeHandler 是自定义的一个加解密类型处理器，可以手动指定到属性上对该属性进行加解密

**如果加在入参的属性上，会对该属性进行自动加密**

**如果在在出参的属性上，会对该属性进行自动解密**

#### 举个栗子

我们有个入参是map的insert 方法
```java
  @Insert({
      "insert into user (`id`, `password`, ",
      "`user_name`, `age`)",
      "values (#{id,jdbcType=BIGINT}, #{password,jdbcType=VARCHAR}, ",
       // 对 userName 指定 typeHandler
      "#{userName,jdbcType=VARCHAR,typeHandler=cryptTypeHandler}, #{age,jdbcType=INTEGER})"
  })
  @Options(useGeneratedKeys = true)
  int insertWithMap(Map<String,Object> record);
```
我们对 userName 手动指定了 typeHandler 为 cryptTypeHandler，那么该参数保存数据库时会被自动加密

再来看一个返回值为 map 的查询方法
```java
  @Select({
    "select",
    "`id`, `password`, `user_name`, `age`",
    "from user",
    "where `id` = #{id,jdbcType=BIGINT}"
})
@Results({
    @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
    @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
    // 对 userName 指定 typeHandler
    @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR, typeHandler = CryptTypeHandler.class),
    @Result(column="age", property="age", jdbcType=JdbcType.INTEGER)
})
  Map<String, Object> selectByPrimaryKeyWithReturnMap(Long id);
```
我们对 userName 手动指定了 typeHandler 为 cryptTypeHandler，那么该参数返回时会被自动解密

## 加密扩展
加密算法默认使用的是 AES，模式为 AES/ECB/PKCS5Padding

你如果想自定义自己的算法，继承 BaseCrypt 类即可

```java
public class CustomCrypt extends BaseCrypt {

  @Override
  public String encrypt(String content) {
    String secretKey = getSecretKey();
    // 自定义字段的加密算法
  }

  @Override
  public String decrypt(String content) {
    String secretKey = getSecretKey();
    // 自定义字段的解密算法
  }

  @Override
  boolean checkSecretKey() {
    // 添加自己的密钥校验逻辑
    return true;
  }
}
```

然后把你的自定义算法类全限定名配置到配置文件里

```
# 配置自定义的加解密类
mybatis.encrypt.cryptClass=xxxxxx
```

## 更多示例

以上示例使用的是 mybatis 注解方式，xml 的方式基本也一样，这里不再赘述

如果你想看更多使用示例，请参考 mybatis-encrypt-test 模块
