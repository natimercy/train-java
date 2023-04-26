package org.natimercy.generator.handler;

import java.time.format.DateTimeFormatter;

/**
 * @author hq
 * @date 2020-12-08
 */
public interface TypeHandler<T> {

    /**
     * 日期格式化：yyyy-MM-dd
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 日期时间格式化：yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 时间格式化：HH:mm:ss
     */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * 转换值
     *
     * @param value value
     * @return Object
     */
    Object converter(Object value);

}
