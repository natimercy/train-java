package com.self.retry.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * todo
 *
 * @author natimercy
 * @version 1.0.0
 * @since 2022-09-05
 */
@Configuration
public class RetryTemplateConfig {

    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(1000L);

        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();


        return retryTemplate;
    }

}
