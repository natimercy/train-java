package com.markly.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * todo
 *
 * @author: spearmint
 * @date: 2021/3/20
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        )
})
public class TestInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 被代理对象
        Object target = invocation.getTarget();
        // 代理方法
        Method method = invocation.getMethod();
        //方法参数
        Object[] args = invocation.getArgs();
        // do something ...... 方法拦截前执行代码块
        // 执行原来方法
        Object result = invocation.proceed();
        // do something .......方法拦截后执行代码块
        return result;
    }

    @Override
    public Object plugin(Object target) {
        System.out.println("MySecondPlugin为目标对象" + target + "创建代理对象");
        // this表示当前拦截器，target表示目标对象，
        // wrap方法利用mybatis封装的方法为目标对象创建代理对象（没有拦截的对象会直接返回，不会创建代理对象）
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
