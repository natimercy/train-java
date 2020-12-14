package com.markly.context;

import com.markly.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author hq
 * @date 2020-11-20
 */
@Component
@Order(value = 2)
public class IRunner implements CommandLineRunner {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("1111111111111");
        System.out.println(sysUserService.findUserByFirstDb(1L));
    }

}
