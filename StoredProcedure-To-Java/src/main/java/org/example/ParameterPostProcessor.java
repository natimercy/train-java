package org.example;

/**
 * @author hq
 * @date 2020-12-25
 */
public interface ParameterPostProcessor {


    /**
     * 是否需要处理前端传过来的查询条件
     *
     * @return 默认false不处理，true处理
     */
    default boolean isHandle() {
        return false;
    }


    /**
     * 参数处理
     */
    void postHandle();

}
