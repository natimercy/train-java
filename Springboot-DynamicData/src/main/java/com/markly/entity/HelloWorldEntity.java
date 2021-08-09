package com.markly.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;

import java.sql.Date;

/**
 *
 * @author hq
 * @date 2021-08-09
 */
@FluentMybatis
public class HelloWorldEntity {

    private Long id;

    private String sayHello;

    private String yourName;

    private Date gmtCreate;

    private Date gmtModified;

    private Boolean isDeleted;

}
