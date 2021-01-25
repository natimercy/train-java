package com.markly.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markly.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户 Mapper 接口
 *
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    String getName(@Param("id") Long id);

}
