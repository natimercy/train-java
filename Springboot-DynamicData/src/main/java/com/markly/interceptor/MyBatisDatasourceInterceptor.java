package com.markly.interceptor;

import com.mchange.v2.c3p0.impl.NewProxyConnection;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.BeanFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

@Intercepts(@Signature(type = StatementHandler.class,method = "query",args = {Statement.class, ResultSet.class}))
public class MyBatisDatasourceInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Statement statement = (Statement) invocation.getArgs()[0];
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatement = SystemMetaObject.forObject(statement);
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        BeanFactory factory = null;
        DataSource dataSource = (DataSource) factory.getBean("routDataSource");
        // c3p0数据源
        NewProxyConnection proxy = (NewProxyConnection) dataSource.getConnection();
        Field inner = proxy.getClass().getDeclaredField("inner");
        // 替换数据源的操作
        metaStatement.setValue("statement.inner.connection", inner.get(proxy));

        return null;
    }

    @Override
    public Object plugin(Object o) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
