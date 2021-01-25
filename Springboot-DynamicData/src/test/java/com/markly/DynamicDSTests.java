package com.markly;

import com.markly.datasource.DataSourceAspect;
import com.markly.entity.SysUser;
import com.markly.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan({"com.markly.service.impl"})
public class DynamicDSTests {
    private final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Autowired
    private SysUserService userService;

    @Test
    public void contextLoads() {
        SysUser user = userService.getById(1);
        logger.info(user.toString());
    }

    @Test
    public void test() {
        // localhost:3306/ds1
        SysUser user2 = userService.findUserBySecondDb(2);
        logger.info("第二个数据库 : [{}]", user2.toString());
        // localhost:3306/ds0
        SysUser user = userService.findUserByFirstDb(1);
        logger.info("第一个数据库 : [{}]", user);
    }
}
