package com.markly.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markly.mapper.SysUserMapper;
import com.markly.datasource.DataSourceNames;
import com.markly.datasource.TargetDataSource;
import com.markly.entity.SysUser;
import com.markly.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private TestServiceImpl testService;

    /**
     * 在切面中，如果没有指定，就使用第一个数据源
     * @param id
     * @return
     */
    @Override
    public SysUser findUserByFirstDb(long id) {
        return this.baseMapper.selectById(id);
    }

    @TargetDataSource(name = DataSourceNames.SECOND)
    @Override
    public SysUser findUserBySecondDb(long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public String getName(Long id) {
        return baseMapper.getName(id);
    }
}
