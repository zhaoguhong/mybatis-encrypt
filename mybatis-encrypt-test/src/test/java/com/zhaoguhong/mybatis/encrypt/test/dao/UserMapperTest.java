package com.zhaoguhong.mybatis.encrypt.test.dao;


import com.zhaoguhong.mybatis.encrypt.crypt.Crypt;
import com.zhaoguhong.mybatis.encrypt.crypt.CryptFactory;
import com.zhaoguhong.mybatis.encrypt.test.MybatisEncryptTestApplication;
import com.zhaoguhong.mybatis.encrypt.test.entity.User;
import com.zhaoguhong.mybatis.encrypt.test.util.UserGenerator;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisEncryptTestApplication.class)
public class UserMapperTest {

  @Autowired
  private UserMapper userMapper;
  @Autowired
  private UserJdbcDao userJdbcDao;

  private Crypt crypt;

  @Before
  public void before() {
    crypt = CryptFactory.getCrypt();
  }

  @Test
  public void testCryptFieldAnnotation() {
    User user = UserGenerator.generate();
    userMapper.insert(user);
    myAssert(user);

    User updateUser = UserGenerator.generate();
    updateUser.setId(user.getId());
    userMapper.updateByPrimaryKeySelective(updateUser);
    myAssert(updateUser);

    updateUser = UserGenerator.generate();
    updateUser.setId(user.getId());
    userMapper.updateByPrimaryKey(updateUser);
    myAssert(updateUser);
  }

  public void myAssert(User user) {
    // 校验入库的数据已经加密成功，并校验查询出来的数据已经解密成功，并和原来数据一模一样
    Assert
        .assertEquals(crypt.encrypt(user.getUserName()), userJdbcDao.getUserNameById(user.getId()));
    // 校验查询出来的数据已经解密成功，并和原来数据一模一样
    Assert.assertEquals(user, userMapper.selectByPrimaryKey(user.getId()));
  }


  @Test
  public void testCryptTypeHandler() {
    Map<String, Object> user = UserGenerator.generateMap();
    userMapper.insertWithMap(user);
    // 校验入库的数据已经加密成功，并校验查询出来的数据已经解密成功，并和原来数据一模一样
    Assert
        .assertEquals(crypt.encrypt(MapUtils.getString(user,"userName")), userJdbcDao.getUserNameById(MapUtils.getLong(user,"id")
        ));
    // 校验查询出来的数据已经解密成功，并和原来数据一模一样
    Assert.assertEquals(user.toString(), userMapper.selectByPrimaryKeyWithReturnMap(MapUtils.getLong(user,"id")).toString());
  }

}
