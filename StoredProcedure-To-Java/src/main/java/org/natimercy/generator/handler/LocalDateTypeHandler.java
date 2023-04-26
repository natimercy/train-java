package org.natimercy.generator.handler;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

/**
 * @author hq
 * @date 2020-12-08
 */
public class LocalDateTypeHandler implements TypeHandler<LocalDate> {

    @Override
    public Object converter(Object value) {
        String format = DATE_FORMATTER.format((TemporalAccessor) value);
        return "'" + format + "'";
    }

}
