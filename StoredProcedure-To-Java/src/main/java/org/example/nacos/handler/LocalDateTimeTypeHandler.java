package org.example.nacos.handler;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;

/**
 * @author hq
 * @date 2020-12-08
 */
public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime> {

    @Override
    public Object converter(Object value) {
        String format = TypeHandler.DATE_TIME_FORMATTER.format((TemporalAccessor) value);
        return "'" + format + "'";
    }

}
