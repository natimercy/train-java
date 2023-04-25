package com.self.retry.service;

import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * todo
 *
 * @author natimercy
 * @version 1.0.0
 * @since 2022-09-05
 */
@Service
public class RetryService {

    @Retryable(RemoteAccessException.class)
    public void service() {

    }

    @Recover
    public void recover(RemoteAccessException e) {

    }


}
