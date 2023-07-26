package com.example.java.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * TODO
 *
 * @author <a href="mailto:mercyblitz@gmail.com">natimercy</a>
 * @since 1.0.0
 */
public class ProxyTest {
    public interface DemoService {
        String getName();
    }

    public interface GameService {
        void run();

        void birth();
    }


    public static class ServiceInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("method = " + method.getName());
            if ("getName".equals(method.getName())) {
                return "demoService";
            }
            return null;
        }
    }

    public static void main(String[] args) {
        System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");

        final ServiceInvocationHandler serviceInvocationHandler = new ServiceInvocationHandler();
        final DemoService demoService = (DemoService) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{DemoService.class, GameService.class},
                serviceInvocationHandler);
        final String name = demoService.getName();
        System.out.println("name = " + name);
    }
}