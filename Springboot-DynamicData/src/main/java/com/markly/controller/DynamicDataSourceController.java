package com.markly.controller;

import com.markly.entity.SysUser;
import com.markly.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author hq
 * @date 2020-11-09
 */
@RestController
@RequestMapping("/user")
public class DynamicDataSourceController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/selectFirst/{id}")
    public SysUser selectFirst(@PathVariable(name = "id") Long id) {
        return sysUserService.findUserByFirstDb(id);
    }

    @GetMapping("/selectSecond/{id}")
    public SysUser selectSecond(@PathVariable(name = "id") Long id) {
        return sysUserService.findUserBySecondDb(id);
    }

    @GetMapping("/getName/{id}")
    public String getName(@PathVariable(name = "id") Long id) {
        return sysUserService.getName(id);
    }

    @PostMapping("/insert")
    public Boolean getName(@RequestBody SysUser sysUser) {
        sysUser.setUserId(UUID.randomUUID().toString());
        return sysUserService.save(sysUser);
    }

}
