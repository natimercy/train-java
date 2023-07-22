package com.natimercy.service.provider.exception;

import java.util.function.Predicate;

/**
 * TODO
 *
 * @author <a href="mailto:mercyblitz@gmail.com">natimercy</a>
 * @since 1.0.0
 */
public class RecordFailurePredicate implements Predicate<Throwable> {

    @Override
    public boolean test(Throwable throwable) {
        return !(throwable instanceof BusinessException);
    }
}
