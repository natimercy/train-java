package org.example.nacos.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * TypeHandler注册器
 *
 * @author hq
 * @date 2020-12-08
 */
public final class TypeHandlerRegistry {

    private final Map<Class<?>, TypeHandler<?>> typeHandlerMap = new HashMap<>();

    private final static TypeHandlerRegistry INSTANCE = new TypeHandlerRegistry();

    private TypeHandlerRegistry() {
        typeHandlerMap.put(LocalDate.class, new LocalDateTypeHandler());
        typeHandlerMap.put(LocalDateTime.class, new LocalDateTimeTypeHandler());
    }

    public static TypeHandlerRegistry getInstance() {
        return INSTANCE;
    }

    public Map<Class<?>, TypeHandler<?>> getTypeHandlerMap() {
        return typeHandlerMap;
    }

}
